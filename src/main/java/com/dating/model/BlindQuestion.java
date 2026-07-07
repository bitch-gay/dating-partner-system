package com.dating.model;

import java.time.LocalDateTime;

public class BlindQuestion {
    private Integer id;
    private Integer matchId;
    private Integer fromUserId;
    private String questionContent;
    private String userAnswer;
    private LocalDateTime createdAt;

    public BlindQuestion() {}

    public BlindQuestion(Integer matchId, Integer fromUserId, String questionContent, String userAnswer) {
        this.matchId = matchId;
        this.fromUserId = fromUserId;
        this.questionContent = questionContent;
        this.userAnswer = userAnswer;
    }

    // --- Getter and Setter ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public Integer getFromUserId() { return fromUserId; }
    public void setFromUserId(Integer fromUserId) { this.fromUserId = fromUserId; }
    public String getQuestionContent() { return questionContent; }
    public void setQuestionContent(String questionContent) { this.questionContent = questionContent; }
    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}