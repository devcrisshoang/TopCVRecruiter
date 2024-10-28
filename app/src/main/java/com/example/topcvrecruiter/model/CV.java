package com.example.topcvrecruiter.model;

import java.io.Serializable;

public class CV implements Serializable {
    private int id;
    private String applicant_Name;
    private String email;
    private String phone_Number;
    private String education;
    private String skills;
    private String certificate;
    private String job_Applying;
    private String introduction;
    private String image;
    private int iD_Applicant;


    public CV(int id, String applicant_Name, String email, String phone_Number, String education, String skills, String certificate, String job_Applying, String introduction, String image, int iD_Applicant) {
        this.id = id;
        this.applicant_Name = applicant_Name;
        this.email = email;
        this.phone_Number = phone_Number;
        this.education = education;
        this.skills = skills;
        this.certificate = certificate;
        this.job_Applying = job_Applying;
        this.introduction = introduction;
        this.image = image;
        this.iD_Applicant = iD_Applicant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicant_Name() {
        return applicant_Name;
    }

    public void setApplicant_Name(String applicant_Name) {
        this.applicant_Name = applicant_Name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getJob_Applying() {
        return job_Applying;
    }

    public void setJob_Applying(String job_Applying) {
        this.job_Applying = job_Applying;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getiD_Applicant() {
        return iD_Applicant;
    }

    public void setiD_Applicant(int iD_Applicant) {
        this.iD_Applicant = iD_Applicant;
    }
}

