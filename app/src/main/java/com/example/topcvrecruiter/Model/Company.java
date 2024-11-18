package com.example.topcvrecruiter.Model;

import com.google.gson.annotations.SerializedName;

public class Company {
    @SerializedName("id")
    private int id;
    @SerializedName("company_Name")
    private String name;
    @SerializedName("company_Address")
    private String address;
    @SerializedName("hotline")
    private String hotline;
    @SerializedName("field")
    private String field;
    @SerializedName("image")
    private String image;
    @SerializedName("green_Badge")
    private boolean green_Badge;
    
    public Company() {

    }

    public Company(String name, String address, String hotline, String field, String image, boolean green_Badge) {
        this.name = name;
        this.address = address;
        this.hotline = hotline;
        this.field = field;
        this.image = image;
        this.green_Badge = green_Badge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isGreen_Badge() {
        return green_Badge;
    }

    public void setGreen_Badge(boolean green_Badge) {
        this.green_Badge = green_Badge;
    }
}

