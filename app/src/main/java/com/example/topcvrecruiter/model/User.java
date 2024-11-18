package com.example.topcvrecruiter.Model;

public class User {
    private int id;
    private String username;
    private String password;
    private String imageBackground;
    private String avatar;
    private String uid;
    private boolean isApplicant;
    private boolean isRecruiter;

    public boolean isApplicant() {
        return isApplicant;
    }

    public void setApplicant(boolean applicant) {
        isApplicant = applicant;
    }

    public boolean isRecruiter() {
        return isRecruiter;
    }

    public void setRecruiter(boolean recruiter) {
        isRecruiter = recruiter;
    }

    public User(String username, String password, String imageBackground, String avatar, String uid, boolean isApplicant, boolean isRecruiter) {
        this.username = username;
        this.password = password;
        this.imageBackground = imageBackground;
        this.avatar = avatar;
        this.uid = uid;
        this.isApplicant = isApplicant;
        this.isRecruiter = isRecruiter;
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

    public String getImageBackground() {
        return imageBackground;
    }

    public void setImageBackground(String imageBackground) {
        this.imageBackground = imageBackground;
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

}
