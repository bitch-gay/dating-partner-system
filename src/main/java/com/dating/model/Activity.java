package com.dating.model;

import java.time.LocalDateTime;

public class Activity {
    private Integer id;
    private Integer publisherId;
    private String title;
    private String type; // 运动/电影/读书/饭局/桌游
    private String genderPreference; // 男/女/不限/LGBTQ+友好
    private String locationDesc;
    private LocalDateTime activityTime;
    private Integer maxPeople;
    private Integer currentPeople;
    private Integer status; // 0进行中 1满员 2过期
    private LocalDateTime createdAt;

    // 构造方法 (发布时用)
    public Activity(Integer publisherId, String title, String type,
                    String genderPreference, String locationDesc,
                    LocalDateTime activityTime, Integer maxPeople) {
        this.publisherId = publisherId;
        this.title = title;
        this.type = type;
        this.genderPreference = genderPreference;
        this.locationDesc = locationDesc;
        this.activityTime = activityTime;
        this.maxPeople = maxPeople;
        this.currentPeople = 1; // 发布者自己算一个
        this.status = 0;
    }

    public Activity() {}

    // --- Getter and Setter ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPublisherId() { return publisherId; }
    public void setPublisherId(Integer publisherId) { this.publisherId = publisherId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getGenderPreference() { return genderPreference; }
    public void setGenderPreference(String genderPreference) { this.genderPreference = genderPreference; }
    public String getLocationDesc() { return locationDesc; }
    public void setLocationDesc(String locationDesc) { this.locationDesc = locationDesc; }
    public LocalDateTime getActivityTime() { return activityTime; }
    public void setActivityTime(LocalDateTime activityTime) { this.activityTime = activityTime; }
    public Integer getMaxPeople() { return maxPeople; }
    public void setMaxPeople(Integer maxPeople) { this.maxPeople = maxPeople; }
    public Integer getCurrentPeople() { return currentPeople; }
    public void setCurrentPeople(Integer currentPeople) { this.currentPeople = currentPeople; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}