<<<<<<< HEAD
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

    public boolean isIs_Confirm() {
        return is_Confirm;
    }

    public void setIs_Confirm(boolean is_Confirm) {
        this.is_Confirm = is_Confirm;
    }

    public boolean isIs_Registered() {
        return is_Registered;
    }

    public void setIs_Registered(boolean is_Registered) {
        this.is_Registered = is_Registered;
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

    // Getter và Setter

=======
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

>>>>>>> a608556aa8aa7930c56924018cfa3066b5d3328a
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

<<<<<<< HEAD
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

=======
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
>>>>>>> a608556aa8aa7930c56924018cfa3066b5d3328a
}
