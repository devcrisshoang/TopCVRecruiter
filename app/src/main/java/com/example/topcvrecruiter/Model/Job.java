package com.example.topcvrecruiter.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Job implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("image_Id")
    private String image_Id;

    @SerializedName("job_Name")
    private String job_Name;

    @SerializedName("company_Name")
    private String company_Name;

    @SerializedName("working_Experience_Require")
    private String working_Experience_Require;

    @SerializedName("working_Address")
    private String working_Address;

    @SerializedName("salary")
    private String salary;

    @SerializedName("create_Time")
    private String create_Time;

    @SerializedName("application_Date")
    private String application_Date;

    @SerializedName("application_Status")
    private boolean application_Status;

    @SerializedName("iD_Recruiter")
    private int iD_Recruiter;

    // Getters và Setters

    public String getImage_Id() {
        return image_Id;
    }

    public void setImage_Id(String image_Id) {
        this.image_Id = image_Id;
    }

    public String getCompany_Name() {
        return company_Name;
    }

    public void setCompany_Name(String company_Name) {
        this.company_Name = company_Name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
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

    public Job(String image_Id, String job_Name, String company_Name, String working_Experience_Require, String working_Address, String salary, String application_Date, int iD_Recruiter) {
        this.image_Id = image_Id;
        this.job_Name = job_Name;
        this.company_Name = company_Name;
        this.working_Experience_Require = working_Experience_Require;
        this.working_Address = working_Address;
        this.salary = salary;
        this.application_Date = application_Date;
        this.iD_Recruiter = iD_Recruiter;
    }

}
