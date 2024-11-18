package com.example.topcvrecruiter.Model;

import com.google.gson.annotations.SerializedName;

public class Company {
<<<<<<< HEAD
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
=======
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
>>>>>>> a608556aa8aa7930c56924018cfa3066b5d3328a
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
<<<<<<< HEAD
=======
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
>>>>>>> a608556aa8aa7930c56924018cfa3066b5d3328a
    }

    public String getHotline() {
        return hotline;
    }

<<<<<<< HEAD
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

=======
    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getField() {
        return field;
    }

>>>>>>> a608556aa8aa7930c56924018cfa3066b5d3328a
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

