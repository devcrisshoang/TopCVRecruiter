package com.example.topcvrecruiter.model;
public class Company {
    private int id;
    private String company_Name;
    private String company_Address;
    private String hotline;
    private String field;
    private String image;
    private boolean green_Badge;

    public Company(String company_Name, String company_Address, String hotline, String field, String image, boolean green_Badge) {
        this.company_Name = company_Name;
        this.company_Address = company_Address;
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

    public String getCompany_Name() {
        return company_Name;
    }

    public void setCompany_Name(String company_Name) {
        this.company_Name = company_Name;
    }

    public String getCompany_Address() {
        return company_Address;
    }

    public void setCompany_Address(String company_Address) {
        this.company_Address = company_Address;
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

