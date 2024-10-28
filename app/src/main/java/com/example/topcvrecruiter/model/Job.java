package com.example.topcvrecruiter.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Job implements Serializable {
    private int id;
    private String job_Name;
    private String working_Experience_Require;
    private String working_Address;
    private String create_Time;
    private String application_Date;
    private boolean application_Status;
    private int iD_Recruiter;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_Name() {
        return job_Name;
    }

    public void setJob_Name(String job_Name) {
        this.job_Name = job_Name;
    }

    public String getWorking_Experience_Require() {
        return working_Experience_Require;
    }

    public void setWorking_Experience_Require(String working_Experience_Require) {
        this.working_Experience_Require = working_Experience_Require;
    }

    public String getWorking_Address() {
        return working_Address;
    }

    public void setWorking_Address(String working_Address) {
        this.working_Address = working_Address;
    }

    public String getCreate_Time() {
        return create_Time;
    }

    public void setCreate_Time(String create_Time) {
        this.create_Time = create_Time;
    }

    public String getApplication_Date() {
        return application_Date;
    }

    public void setApplication_Date(String application_Date) {
        this.application_Date = application_Date;
    }

    public boolean isApplication_Status() {
        return application_Status;
    }

    public void setApplication_Status(boolean application_Status) {
        this.application_Status = application_Status;
    }

    public int getiD_Recruiter() {
        return iD_Recruiter;
    }

    public void setiD_Recruiter(int iD_Recruiter) {
        this.iD_Recruiter = iD_Recruiter;
    }

    public Job(int id, String job_Name, String working_Experience_Require, String working_Address, String create_Time, String application_Date, boolean application_Status, int iD_Recruiter) {
        this.id = id;
        this.job_Name = job_Name;
        this.working_Experience_Require = working_Experience_Require;
        this.working_Address = working_Address;
        this.create_Time = create_Time;
        this.application_Date = application_Date;
        this.application_Status = application_Status;
        this.iD_Recruiter = iD_Recruiter;
    }
}
