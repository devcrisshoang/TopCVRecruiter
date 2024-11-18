package com.example.topcvrecruiter.Model;

public class CompanyInformationDetails {
    private int idCompanyInformationDetails;
    private String website;
    private int taxID;
    private String dateFounded;
    private int idCompany;

    // Constructor không tham số
    public CompanyInformationDetails() {}

    // Constructor với các tham số
    public CompanyInformationDetails(int idCompanyInformationDetails, String website, int taxID, String dateFounded, int idCompany) {
        this.idCompanyInformationDetails = idCompanyInformationDetails;
        this.website = website;
        this.taxID = taxID;
        this.dateFounded = dateFounded;
        this.idCompany = idCompany;
    }

    // Getter và Setter
    public int getIdCompanyInformationDetails() {
        return idCompanyInformationDetails;
    }

    public void setIdCompanyInformationDetails(int idCompanyInformationDetails) {
        this.idCompanyInformationDetails = idCompanyInformationDetails;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public String getDateFounded() {
        return dateFounded;
    }

    public void setDateFounded(String dateFounded) {
        this.dateFounded = dateFounded;
    }

    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }
}
