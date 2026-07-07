package com.dating.model;

import java.time.LocalDateTime;

public class User {
    private Integer id;
    private String phone;
    private String password;
    private String nickname;
    private Integer gender; // 0女 1男 2非二元
    private String sexualOrientation; // 异性/同性/双性/泛性
    private Integer showGender; // 0隐藏 1展示
    private Integer age;
    private String city;
    private String mbti;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;

    // 全参构造、无参构造
    public User() {}

    public User(String phone, String password, String nickname, Integer gender,
                String sexualOrientation, Integer age, String city) {
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.sexualOrientation = sexualOrientation;
        this.age = age;
        this.city = city;
        this.mbti = "ENFP"; // 默认性格，后续可做测试
    }

    // --- 以下为所有字段的 Getter 和 Setter (IDEA快捷键: Alt+Insert -> Getter and Setter) ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public String getSexualOrientation() { return sexualOrientation; }
    public void setSexualOrientation(String sexualOrientation) { this.sexualOrientation = sexualOrientation; }
    public Integer getShowGender() { return showGender; }
    public void setShowGender(Integer showGender) { this.showGender = showGender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getMbti() { return mbti; }
    public void setMbti(String mbti) { this.mbti = mbti; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}