package com.example.topcvrecruiter.Model;

import com.google.gson.annotations.SerializedName;

public class Recruiter {

    @SerializedName("id")
    private int id;

    @SerializedName("recruiter_Name")
    private String recruiterName;

    @SerializedName("phone_Number")
    private String phoneNumber;

    @SerializedName("iD_Company")
    private int idCompany;

    @SerializedName("email_Address")
    private String emailAddress;

    @SerializedName("iD_User")
    private int idUser;

    @SerializedName("front_Image")
    private String frontImage;

    @SerializedName("back_Image")
    private String backImage;

    @SerializedName("is_Registered")
    private boolean is_Registered;

    @SerializedName("is_Confirm")
    private boolean is_Confirm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public boolean isIs_Registered() {
        return is_Registered;
    }

    public void setIs_Registered(boolean is_Registered) {
        this.is_Registered = is_Registered;
    }

    public boolean isIs_Confirm() {
        return is_Confirm;
    }

    public void setIs_Confirm(boolean is_Confirm) {
        this.is_Confirm = is_Confirm;
    }

    // Constructor mặc định
    public Recruiter() {
    }

    // Constructor có tham số
    public Recruiter(int id, String recruiterName, String phoneNumber, int idCompany, String emailAddress, int idUser, String frontImage, String backImage) {
        this.id = id;
        this.recruiterName = recruiterName;
        this.phoneNumber = phoneNumber;
        this.idCompany = idCompany;
        this.emailAddress = emailAddress;
        this.idUser = idUser;
        this.frontImage = frontImage;
        this.backImage = backImage;
    }
}