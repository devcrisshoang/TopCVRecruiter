package com.example.topcvrecruiter.Model;

public class JobDetails {
    private int id;
    private String job_Description;
    private String skill_Require;
    private String benefit;
    private String gender_Require;
    private String working_Time;
    private String working_Method;
    private String working_Position;
    private String number_Of_People;
    private int iD_Job;

    public String getNumber_Of_People() {
        return number_Of_People;
    }

    public void setNumber_Of_People(String number_Of_People) {
        this.number_Of_People = number_Of_People;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_Description() {
        return job_Description;
    }

    public void setJob_Description(String job_Description) {
        this.job_Description = job_Description;
    }

    public String getSkill_Require() {
        return skill_Require;
    }

    public void setSkill_Require(String skill_Require) {
        this.skill_Require = skill_Require;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getGender_Require() {
        return gender_Require;
    }

    public void setGender_Require(String gender_Require) {
        this.gender_Require = gender_Require;
    }

    public String getWorking_Time() {
        return working_Time;
    }

    public void setWorking_Time(String working_Time) {
        this.working_Time = working_Time;
    }

    public String getWorking_Method() {
        return working_Method;
    }

    public void setWorking_Method(String working_Method) {
        this.working_Method = working_Method;
    }

    public String getWorking_Position() {
        return working_Position;
    }

    public void setWorking_Position(String working_Position) {
        this.working_Position = working_Position;
    }

    public int getiD_Job() {
        return iD_Job;
    }

    public void setiD_Job(int iD_Job) {
        this.iD_Job = iD_Job;
    }

    public JobDetails(String job_Description, String skill_Require, String benefit,String gender_Require, String working_Time, String working_Method, String working_Position,String number_Of_People, int iD_Job) {
        this.job_Description = job_Description;
        this.skill_Require = skill_Require;
        this.benefit = benefit;
        this.gender_Require = gender_Require;
        this.working_Time = working_Time;
        this.working_Method = working_Method;
        this.working_Position = working_Position;
        this.number_Of_People = number_Of_People;
        this.iD_Job = iD_Job;
    }
}
