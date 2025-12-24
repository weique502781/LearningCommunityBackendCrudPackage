package com.example.app.controller;

import com.example.app.model.*;
import com.example.app.service.AdminService;
import com.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;//处理管理员相关操作

    @Autowired
    private UserService userService;//用户权限验证

    // 权限检查中间件（这里简化处理，实际应该使用Spring Security或拦截器）
    private void checkAdminPermission(Long userId) {
        if (!userService.isAdmin(userId)) {
            throw new RuntimeException("没有管理员权限");
        }
    }

    // 用户管理
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestHeader("X-User-Id") Long userId) {
        checkAdminPermission(userId);

        List<User> users = adminService.getAllUsers();
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("data", users);
        result.put("count", users.size());
        return ResponseEntity.ok(result);
    }

    // 根据角色获取用户
    @GetMapping("/users/role/{role}")
    public ResponseEntity<Map<String, Object>> getUsersByRole(
            @PathVariable String role,
            @RequestHeader("X-User-Id") Long userId) {
        checkAdminPermission(userId);

        List<User> users = adminService.getUsersByRole(role);
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("data", users);
        return ResponseEntity.ok(result);
    }

    // 更新用户角色
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        String newRole = request.get("role");
        adminService.updateUserRole(userId, newRole);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "用户角色更新成功");
        return ResponseEntity.ok(result);
    }

    // 用户封禁与解封
    @PostMapping("/users/{userId}/ban")
    public ResponseEntity<Map<String, Object>> banUser(
            @PathVariable Long userId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.banUser(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "用户已封禁");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/{userId}/unban")
    public ResponseEntity<Map<String, Object>> unbanUser(
            @PathVariable Long userId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.unbanUser(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "用户已解封");
        return ResponseEntity.ok(result);
    }

    // 内容审核
    @GetMapping("/content/pending")
    public ResponseEntity<Map<String, Object>> getPendingContent(@RequestHeader("X-User-Id") Long userId) {
        checkAdminPermission(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("pendingQuestions", adminService.getPendingQuestions());
        result.put("pendingAnswers", adminService.getPendingAnswers());
        result.put("pendingResources", adminService.getPendingResources());
        return ResponseEntity.ok(result);
    }

    // 问题审核
    @PostMapping("/questions/{questionId}/approve")
    public ResponseEntity<Map<String, Object>> approveQuestion(
            @PathVariable Long questionId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.approveQuestion(questionId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "问题审核通过");
        return ResponseEntity.ok(result);
    }

    //拒绝问题
    @PostMapping("/questions/{questionId}/reject")
    public ResponseEntity<Map<String, Object>> rejectQuestion(
            @PathVariable Long questionId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.rejectQuestion(questionId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "问题已拒绝");
        return ResponseEntity.ok(result);
    }

    // 回答审核
    @PostMapping("/answers/{answerId}/approve")
    public ResponseEntity<Map<String, Object>> approveAnswer(
            @PathVariable Long answerId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.approveAnswer(answerId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "回答审核通过");
        return ResponseEntity.ok(result);
    }

    //拒绝回答
    @PostMapping("/answers/{answerId}/reject")
    public ResponseEntity<Map<String, Object>> rejectAnswer(
            @PathVariable Long answerId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.rejectAnswer(answerId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "回答已拒绝");
        return ResponseEntity.ok(result);
    }

    // 资源审核
    @PostMapping("/resources/{resourceId}/approve")
    public ResponseEntity<Map<String, Object>> approveResource(
            @PathVariable Long resourceId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.approveResource(resourceId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "资源审核通过");
        return ResponseEntity.ok(result);
    }

    //拒绝资源
    @PostMapping("/resources/{resourceId}/reject")
    public ResponseEntity<Map<String, Object>> rejectResource(
            @PathVariable Long resourceId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.rejectResource(resourceId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "资源已拒绝");
        return ResponseEntity.ok(result);
    }

    // 内容删除
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Map<String, Object>> deleteQuestion(
            @PathVariable Long questionId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.deleteQuestion(questionId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "问题已删除");
        return ResponseEntity.ok(result);
    }

    // 删除回答
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Map<String, Object>> deleteAnswer(
            @PathVariable Long answerId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.deleteAnswer(answerId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "回答已删除");
        return ResponseEntity.ok(result);
    }

    // 删除资源
    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<Map<String, Object>> deleteResource(
            @PathVariable Long resourceId,
            @RequestHeader("X-Admin-Id") Long adminId) {
        checkAdminPermission(adminId);

        adminService.deleteResource(resourceId);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("message", "资源已删除");
        return ResponseEntity.ok(result);
    }

    // 系统统计
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats(@RequestHeader("X-User-Id") Long userId) {
        checkAdminPermission(userId);

        AdminStats stats = adminService.getSystemStats();

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("stats", stats);
        return ResponseEntity.ok(result);
    }

    // 获取最近几天的日活跃用户统计
    @GetMapping("/stats/daily")
    public ResponseEntity<Map<String, Object>> getDailyStats(
            @RequestParam(defaultValue = "7") int days,
            @RequestHeader("X-User-Id") Long userId) {
        checkAdminPermission(userId);

        Map<String, Long> dailyStats = adminService.getDailyStats(days);

        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("dailyStats", dailyStats);
        result.put("days", days);
        return ResponseEntity.ok(result);
    }
}
