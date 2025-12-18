// AdminStats.java - 管理员统计模型
package com.example.app.model;

import java.util.Map;

public class AdminStats {
    private Long totalUsers;
    private Long totalQuestions;
    private Long totalAnswers;
    private Long totalResources;
    private Long pendingReviews;
    private Map<String, Long> userGrowth;
    private Map<String, Long> contentGrowth;

    // getter和setter
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Long totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public Long getTotalResources() {
        return totalResources;
    }

    public void setTotalResources(Long totalResources) {
        this.totalResources = totalResources;
    }

    public Long getPendingReviews() {
        return pendingReviews;
    }

    public void setPendingReviews(Long pendingReviews) {
        this.pendingReviews = pendingReviews;
    }

    public Map<String, Long> getUserGrowth() {
        return userGrowth;
    }

    public void setUserGrowth(Map<String, Long> userGrowth) {
        this.userGrowth = userGrowth;
    }

    public Map<String, Long> getContentGrowth() {
        return contentGrowth;
    }

    public void setContentGrowth(Map<String, Long> contentGrowth) {
        this.contentGrowth = contentGrowth;
    }
}
