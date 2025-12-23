# LearningCommunity 项目改动总结（本次协作）

> 本文件总结了你在本次对话里让我完成的所有工作：MySQL 连接/导入、后端跨域与鉴权、前端 UI 与交互修复、内容审核可见性权限、搜索权限、以及服务验证方式。

## 1. 数据库（MySQL）

- **修复连接配置**
  - 更新后端 `application.properties` 中的 MySQL 密码为你提供的 `Mysql123`，确保后端能连上本机 MySQL。
- **导入测试数据与索引**
  - 调整 `test_data.sql`，移除会导致导入失败的 `DROP INDEX`（索引不存在时会报错）。
  - 保留/确保 `questions(title, content)` 的 `FULLTEXT` 索引，用于搜索功能。

## 2. 后端（Spring Boot / MyBatis）

### 2.1 CORS 跨域
- 在 `WebConfig.java` 中增加全局 CORS 配置，允许前端（localhost:3000）访问后端（localhost:8081）。
- 顺手处理 IDE 的 `@NonNull` 提示：给 `addCorsMappings` 的参数加上 `@NonNull`。

### 2.2 登录/注册（含密码）
- **登录**：`AuthController.login` 增强为校验 `email + password`，否则返回 `invalid credentials`。
- **注册**：前端注册请求补充 `password` 字段，避免数据库 `password` 非空约束导致注册失败。

> 注意：当前鉴权依然是“简化版”（请求头传 `X-User-Id`），并非完整的 Spring Security/JWT 校验拦截；本项目原本就是轻量实现。

### 2.3 内容审核可见性权限（核心需求）
你提出的规则：
- **未登录/普通用户**：只能看到 `APPROVED` 内容。
- **内容作者**：能看到自己发布的所有内容（包括 `PENDING/REJECTED`），以及别人 `APPROVED` 内容。
- **管理员**：能看到所有内容。

为实现该规则，做了以下改造：

#### Questions 列表
- **新增 Mapper 查询**：
  - `QuestionMapper.selectVisible(userId, isAdmin)`
  - MyBatis SQL：非管理员时仅返回 `APPROVED` 或 `user_id = 当前用户`。
- **新增 Service 方法**：
  - `QuestionService.listVisible(userId, isAdmin)`
- **Controller 支持可选用户头**：
  - `GET /api/questions` 接收可选 `X-User-Id`，判断是否管理员后返回“可见列表”。

#### Answers 列表
- **新增 Mapper 查询**：
  - `AnswerMapper.selectVisibleByQuestionId(questionId, userId, isAdmin)`
  - MyBatis SQL：非管理员时仅返回 `APPROVED` 或 `user_id = 当前用户`。
- **新增 Service 方法**：
  - `AnswerService.listVisibleByQuestion(questionId, userId, isAdmin)`
- **Controller 支持可选用户头**：
  - `GET /api/answers/question/{qid}` 接收可选 `X-User-Id`。

#### 问题详情接口（补齐遗漏）
- 原实现无权限会抛异常导致 **500**。
- 现在调整为：
  - **不存在**：`404 NOT_FOUND`
  - **无权限**：`403 FORBIDDEN`
  - **可访问**：`200 OK`

### 2.4 搜索结果的可见性
- 最初搜索只返回 `APPROVED`；后来按整体权限模型进一步增强。
- `SearchController`：支持可选 `X-User-Id`，并判断管理员。
- `SearchService`：增加参数 `(userId, isAdmin)` 并传给 Mapper。
- `SearchMapper`：将搜索 SQL 改为动态权限过滤：
  - 管理员：可搜索全部
  - 非管理员：仅 `APPROVED` 或 `user_id = 当前用户`

### 2.5 管理后台安全性检查
- `AdminController` 所有管理接口都基于 `X-User-Id` / `X-Admin-Id` 并调用 `userService.isAdmin(...)` 做权限校验，普通用户不可访问。

### 2.6 管理员删除帖子（问题）
- 后端已提供管理员删除接口：`DELETE /api/admin/questions/{questionId}`（请求头 `X-Admin-Id`）。
- 当前实现为“软删除”：管理员删除时将问题 `status` 更新为 `DELETED`。
- 为避免“删了但仍显示”，已在以下查询中统一排除 `DELETED`：
  - `QuestionMapper.selectById / selectAll / selectByUserId / selectVisible`
  - `SearchMapper.search`（搜索结果也不会包含已删除内容）
  - `AnswerMapper` 相关查询也排除了 `DELETED`（防止已删除回答被展示）

## 3. 前端（单文件 index.html + Tailwind/Lucide）

### 3.1 UI/交互修复
- **回复按钮登录校验**：点击回复（Write a response）时，如未登录会提示并弹出登录框。

### 3.2 登录/注册表单补齐密码
- 登录框增加 `Password` 输入框，登录请求发送 `{ email, password }`。
- 注册框增加 `Password` 输入框，注册请求发送 `{ nickname, email, password, role }`。

### 3.3 权限可见性所需请求头
为配合后端权限过滤：
- `loadQuestions`：请求 `/api/questions` 时在已登录状态带上 `X-User-Id`。
- `showQuestionDetail`：请求 `/api/questions/{id}` 时带上 `X-User-Id`。
- `loadAnswers`：请求 `/api/answers/question/{id}` 时带上 `X-User-Id`。
- `searchQuestions`：请求 `/search` 时带上 `X-User-Id`。

### 3.4 管理员删除帖子（前端入口）
- 管理员 Tab（Admin）中的 `Pending Questions` 列表新增 `Delete` 按钮。
- 问题详情弹窗中，管理员会看到 `Delete` 按钮，可直接删除当前帖子。
- 前端调用：
  - `deleteQuestion(id)` -> `DELETE /api/admin/questions/{id}` 并携带 `X-Admin-Id`
  - 删除成功后自动刷新：Pending 列表与 Questions 列表

## 4. 测试账号与验证方式

### 4.1 测试账号（不写进代码）
- **管理员**：`admin@example.com` / `password123`
- **普通用户**：`alice@example.com` / `password123`
- **普通用户**：`bob@example.com` / `password123`

### 4.2 快速验证建议
- **未登录**
  - 列表/搜索/详情：应只能看到 `APPROVED` 内容；访问非 APPROVED 详情应返回 `403`。
- **作者登录**
  - 能看到自己发布的 `PENDING/REJECTED` 内容（列表、详情、搜索都应一致）。
- **管理员登录**
  - 能看到全部状态。

## 5. 运行方式（本次开发时使用）
- **后端**：在 `backend/` 下运行
  - `mvn spring-boot:run -DskipTests`
- **前端**：在 `frontend/` 下运行
  - `python3 -m http.server 3000`

---

## 当前状态
- 本次你提出的功能点已全部补齐，并完成了基础验证。
- 若你希望进一步“规范化鉴权”（真正基于 JWT + Spring Security，而不是 `X-User-Id`），可以在下一轮再做：增加拦截器/过滤器解析 JWT，并从 token 得到 userId/role，彻底避免前端伪造 `X-User-Id`。
