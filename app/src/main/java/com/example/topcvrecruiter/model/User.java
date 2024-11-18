package com.example.topcvrecruiter.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String image_Background;
    private String avatar;
    private String uid; // Thêm uid
    private Applicant applicant;  // Sửa lại thành Applicant thay vì List<Applicant>

    public User(String username, String password, String image_Background, String avatar, String uid) {
        this.username = username;
        this.password = password;
        this.image_Background = image_Background;
        this.avatar = avatar;
        this.uid = uid;
    }

    public User() {

    }

    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_Background() {
        return image_Background;
    }

    public void setImage_Background(String image_Background) {
        this.image_Background = image_Background;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
