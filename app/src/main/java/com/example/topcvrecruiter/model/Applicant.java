package com.example.topcvrecruiter.model;

import java.io.Serializable;

public class Applicant implements Serializable {
    private int id;
    private String applicant_Name;
    private String phone_number;
    private String email;


    public Applicant(String applicant_Name, String phone_number, String email) {
        this.applicant_Name = applicant_Name;
        this.phone_number = phone_number;
        this.email = email;
    }

    public String getApplicant_Name() {
        return applicant_Name;
    }

    public void setApplicant_Name(String applicant_Name) {
        this.applicant_Name = applicant_Name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
