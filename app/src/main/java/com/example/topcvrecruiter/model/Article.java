package com.example.topcvrecruiter.model;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("id")
    private int id;

    @SerializedName("article_Name")
    private String article_Name;

    @SerializedName("content")
    private String content;

    @SerializedName("create_Time")
    private String create_Time;

    @SerializedName("iD_Recruiter")
    private int iD_Recruiter;

    // Getter và Setter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticle_Name() {
        return article_Name;
    }

    public void setArticle_Name(String article_Name) {
        this.article_Name = article_Name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_Time() {
        return create_Time;
    }

    public void setCreate_Time(String create_Time) {
        this.create_Time = create_Time;
    }

    public int getiD_Recruiter() {
        return iD_Recruiter;
    }

    public void setiD_Recruiter(int iD_Recruiter) {
        this.iD_Recruiter = iD_Recruiter;
    }

    public Article(String article_Name, String content, String create_Time, int iD_Recruiter) {
        this.article_Name = article_Name;
        this.content = content;
        this.create_Time = create_Time;
        this.iD_Recruiter = iD_Recruiter;
    }
}