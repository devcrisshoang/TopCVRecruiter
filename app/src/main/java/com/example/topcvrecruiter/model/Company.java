package com.example.topcvrecruiter.Model;
public class Company {
    private String name;
    private String industry;
    private String badge;
    private int logo;
    private boolean isChecked;

    public void setName(String name) {
        this.name = name;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Company(String name, String industry, String badge, int logo) {
        this.name = name;
        this.industry = industry;
        this.badge = badge;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public String getBadge() {
        return badge;
    }

    public int getLogo() {
        return logo;
    }
}

