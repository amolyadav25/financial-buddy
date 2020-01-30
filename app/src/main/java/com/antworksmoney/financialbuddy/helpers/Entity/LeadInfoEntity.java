package com.antworksmoney.financialbuddy.helpers.Entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class LeadInfoEntity implements Serializable {

    String created_date;
    String leadStatus;
    public String borowwerName;
    String loanType;
    String loanAmount;
    String submittedToBanks;
    String email;
    String city;
    String address;
    String pin;
    String occupation;
    String companyType;
    String companyName;
    String professionType;
    String property_details;
    String property_value;
    String income;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public String getProperty_details() {
        return property_details;
    }

    public void setProperty_details(String property_details) {
        this.property_details = property_details;
    }

    public String getProperty_value() {
        return property_value;
    }

    public void setProperty_value(String property_value) {
        this.property_value = property_value;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getBorowwerName() {
        return borowwerName;
    }

    public void setBorowwerName(String borowwerName) {
        this.borowwerName = borowwerName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getSubmittedToBanks() {
        return submittedToBanks;
    }

    public void setSubmittedToBanks(String submittedToBanks) {
        this.submittedToBanks = submittedToBanks;
    }

    @NonNull
    @Override
    public String toString() {
        return "LeadInfoEntity{" +
                "created_date='" + created_date + '\'' +
                ", leadStatus='" + leadStatus + '\'' +
                ", borowwerName='" + borowwerName + '\'' +
                ", loanType='" + loanType + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", submittedToBanks='" + submittedToBanks + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", pin='" + pin + '\'' +
                ", occupation='" + occupation + '\'' +
                ", companyType='" + companyType + '\'' +
                ", companyName='" + companyName + '\'' +
                ", professionType='" + professionType + '\'' +
                ", property_details='" + property_details + '\'' +
                ", property_value='" + property_value + '\'' +
                ", income='" + income + '\'' +
                '}';
    }
}
