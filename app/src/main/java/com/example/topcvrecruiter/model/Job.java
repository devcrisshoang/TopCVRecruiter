package com.example.topcvrecruiter.model;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {
    private int id;
    private String job_Name;
    private String working_Experience_Require;
    private String working_Address;
    private Date create_Time;
    private Date application_Date;
    private boolean application_Status;
    private int iD_Recruiter;

    public Job(int id, String job_Name, String working_Experience_Require, String working_Address, Date create_Time, Date application_Date, boolean application_Status) {
        this.id = id;
        this.job_Name = job_Name;
        this.working_Experience_Require = working_Experience_Require;
        this.working_Address = working_Address;
        this.create_Time = create_Time;
        this.application_Date = application_Date;
        this.application_Status = application_Status;
    }

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

    public Date getCreate_Time() {
        return create_Time;
    }

    public void setCreate_Time(Date create_Time) {
        this.create_Time = create_Time;
    }

    public Date getApplication_Date() {
        return application_Date;
    }

    public void setApplication_Date(Date application_Date) {
        this.application_Date = application_Date;
    }

    public boolean isApplication_Status() {
        return application_Status;
    }

    public void setApplication_Status(boolean application_Status) {
        this.application_Status = application_Status;
    }
}