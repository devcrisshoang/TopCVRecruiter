package com.example.topcvrecruiter.model;

public class CV {
    private int id;
    private String name;
    private String email;
    private String education;
    private String experience;
    private String skills;
    private String certifications;
    private String position;
    private int image;


    public CV(int id, String name, String email, String education, String experience, String skills, String certifications, String position, int image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.certifications = certifications;
        this.position = position;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

