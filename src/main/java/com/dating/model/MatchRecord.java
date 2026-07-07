package com.dating.model;

import java.time.LocalDateTime;

public class MatchRecord {
    private Integer id;
    private Integer userAId;
    private Integer userBId;
    private Double matchScore;
    private Integer phase; // 0:双盲中 1:已解锁 2:已结束
    private LocalDateTime createdAt;

    public MatchRecord() {}

    public MatchRecord(Integer userAId, Integer userBId, Double matchScore) {
        this.userAId = userAId;
        this.userBId = userBId;
        this.matchScore = matchScore;
        this.phase = 0; // 默认进入双盲阶段
    }

    // --- Getter and Setter (IDEA快捷键 Alt+Insert) ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserAId() { return userAId; }
    public void setUserAId(Integer userAId) { this.userAId = userAId; }
    public Integer getUserBId() { return userBId; }
    public void setUserBId(Integer userBId) { this.userBId = userBId; }
    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }
    public Integer getPhase() { return phase; }
    public void setPhase(Integer phase) { this.phase = phase; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}