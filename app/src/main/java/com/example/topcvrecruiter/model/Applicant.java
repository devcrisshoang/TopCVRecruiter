package com.example.topcvrecruiter.model;

import java.io.Serializable;

public class Applicant implements Serializable {
    private int id;
    private String applicant_Name;
    private String email;
    private String phone_Number;
    private String job_Desire;
    private String working_Location_Desire;
    private String working_Experience;

    public Applicant(int id, String applicant_Name, String email, String phone_number, String job_Desire, String working_Location_Desire, String working_Experience) {
        this.id = id;
        this.applicant_Name = applicant_Name;
        this.email = email;
        this.phone_Number = phone_number;
        this.job_Desire = job_Desire;
        this.working_Location_Desire = working_Location_Desire;
        this.working_Experience = working_Experience;
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

    public String getPhone_number() {
        return phone_Number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_Number = phone_number;
    }

    public String getJob_Desire() {
        return job_Desire;
    }

    public void setJob_Desire(String job_Desire) {
        this.job_Desire = job_Desire;
    }

    public String getWorking_Location_Desire() {
        return working_Location_Desire;
    }

    public void setWorking_Location_Desire(String working_Location_Desire) {
        this.working_Location_Desire = working_Location_Desire;
    }

    public String getWorking_Experience() {
        return working_Experience;
    }

    public void setWorking_Experience(String working_Experience) {
        this.working_Experience = working_Experience;
    }
}
