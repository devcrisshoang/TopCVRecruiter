package com.example.topcvrecruiter.model;

public class User {
    private int id;
    private String username;
    private String password;
    private int iD_SortOfUser;
    private int image_Background;
    private int avatar;

    public User(String username, String password, int iD_SortOfUser, int image_Background, int avatar) {
        this.username = username;
        this.password = password;
        this.iD_SortOfUser = iD_SortOfUser;
        this.image_Background = image_Background;
        this.avatar = avatar;
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

    public int getID_SortOfUser() {
        return iD_SortOfUser;
    }

    public void setID_SortOfUser(int iD_SortOfUser) {
        this.iD_SortOfUser = iD_SortOfUser;
    }

    public int getImage_Background() {
        return image_Background;
    }

    public void setImage_Background(int image_Background) {
        this.image_Background = image_Background;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
