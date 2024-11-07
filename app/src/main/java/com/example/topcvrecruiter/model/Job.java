package com.example.topcvrecruiter.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Job implements Serializable {
    private int id;
    private String image_Id;
    private String job_Name;
    private String company_Name;
    private String working_Experience_Require;
    private String working_Address;
    private String salary;
    private String create_Time;
    private String application_Date;
    private boolean application_Status;

    public Job(int id, String image_Id, String job_Name, String company_Name, String working_Experience_Require, String working_Address, String salary, String create_Time, String application_Date, boolean application_Status) {
        this.id = id;
        this.image_Id = image_Id;
        this.job_Name = job_Name;
        this.company_Name = company_Name;
        this.working_Experience_Require = working_Experience_Require;
        this.working_Address = working_Address;
        this.salary = salary;
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

    public String getImage_Id() {
        return image_Id;
    }

    public void setImage_Id(String image_Id) {
        this.image_Id = image_Id;
    }

    public String getJob_Name() {
        return job_Name;
    }

    public void setJob_Name(String job_Name) {
        this.job_Name = job_Name;
    }

    public String getCompany_Name() {
        return company_Name;
    }

    public void setCompany_Name(String company_Name) {
        this.company_Name = company_Name;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
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

}
