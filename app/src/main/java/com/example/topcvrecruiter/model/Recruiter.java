package com.example.topcvrecruiter.model;

public class Recruiter {
    private int id;
    private String recruiter_Name;
    private String phone_Number;
    private int iD_Company;
    private String email_Address;
    private int iD_User;
    private String front_Image;
    private String back_Image;

    public Recruiter( String recruiter_Name, String phone_Number, int iD_Company, String email_Address, int iD_User, String front_Image, String back_Image) {
        this.recruiter_Name = recruiter_Name;
        this.phone_Number = phone_Number;
        this.iD_Company = iD_Company;
        this.email_Address = email_Address;
        this.iD_User = iD_User;
        this.front_Image = front_Image;
        this.back_Image = back_Image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecruiter_Name() {
        return recruiter_Name;
    }

    public void setRecruiter_Name(String recruiter_Name) {
        this.recruiter_Name = recruiter_Name;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public int getiD_Company() {
        return iD_Company;
    }

    public void setiD_Company(int iD_Company) {
        this.iD_Company = iD_Company;
    }

    public String getEmail_Address() {
        return email_Address;
    }

    public void setEmail_Address(String email_Address) {
        this.email_Address = email_Address;
    }

    public int getiD_User() {
        return iD_User;
    }

    public void setiD_User(int iD_User) {
        this.iD_User = iD_User;
    }

    public String getFront_Image() {
        return front_Image;
    }

    public void setFront_Image(String front_Image) {
        this.front_Image = front_Image;
    }

    public String getBack_Image() {
        return back_Image;
    }

    public void setBack_Image(String back_Image) {
        this.back_Image = back_Image;
    }
}
