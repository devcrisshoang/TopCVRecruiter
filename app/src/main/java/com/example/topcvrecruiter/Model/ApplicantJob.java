package com.example.topcvrecruiter.Model;

import java.io.Serializable;

public class ApplicantJob implements Serializable {
    private int id;
    private String applicant_Name;
    private String email;
    private String phone_Number;
    private String job_Desire;
    private String working_Location_Desire;
    private String working_Experience;
    private boolean isAccepted;
    private boolean isRejected;

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    private String time;

    public ApplicantJob(int id, String applicant_Name, String email, String phone_Number, String job_Desire, String working_Location_Desire, String working_Experience, boolean isAccepted, String time) {
        this.id = id;
        this.applicant_Name = applicant_Name;
        this.email = email;
        this.phone_Number = phone_Number;
        this.job_Desire = job_Desire;
        this.working_Location_Desire = working_Location_Desire;
        this.working_Experience = working_Experience;
        this.isAccepted = isAccepted;
        this.time = time;
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

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
