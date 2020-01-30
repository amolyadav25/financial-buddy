package com.antworksmoney.financialbuddy.helpers.Entity;

import java.io.Serializable;

public class LoanInfoEntity implements Serializable {
    private String name = "";

    private String email = "";

    private String phoneNumber = "";

    private String occupation = "";

    private String qualification = "";

    private String state = "";

    private String city = "";

    private String loanType = "";

    private String loanName = "";

    private String companyType = "";

    private String companyName = "";

    private String loanAmount = "";

    private String gender = "";

    private String dateOfBirth = "";

    private String maritalStatus = "";

    private String nationality = "";

    private String loanId = "";

    private String loanApplyFor = "";

    private String loanImageUrl = "";

    private String propertyType = "";

    private String salaryProcess = "";

    private String EMI = "";

    private String salary = "";

    private String grossTurnOverBeforeLastYear = "";

    private String auditDone = "";

    private String defaultedOnLoan = "";

    private String industryType = "";

    private String netWorth = "";

    private String totalExperience = "";

    private String grossTurnOver = "";

    private String officeOwnerShip = "";

    private String bankName = "";

    private String officePhoneNumber = "";

    private String dateOfIncorporation = "";

    private String CIN = "";

    private String grossTurnOver2 = "";

    private String grossTurnOver3 = "";

    private String checkBounced = "";

    private String companyRatedByAgency = "";

    private String professionType = "";

    private String latitude = "";

    private String longitude = "";

    private String account = "";

    private String emi_date = "";

    private String emi_balance = "";

    private String bIdRegistrationId = "";

    private String emi_interest = "", emi_principal = "";

    public String getEmi_interest() {
        return emi_interest;
    }

    public void setEmi_interest(String emi_interest) {
        this.emi_interest = emi_interest;
    }

    public String getEmi_principal() {
        return emi_principal;
    }

    public void setEmi_principal(String emi_principal) {
        this.emi_principal = emi_principal;
    }

    public String getbIdRegistrationId() {
        return bIdRegistrationId;
    }

    public void setbIdRegistrationId(String bIdRegistrationId) {
        this.bIdRegistrationId = bIdRegistrationId;
    }

    public String getEmi_date() {
        return emi_date;
    }

    public void setEmi_date(String emi_date) {
        this.emi_date = emi_date;
    }

    public String getEmi_balance() {
        return emi_balance;
    }

    public void setEmi_balance(String emi_balance) {
        this.emi_balance = emi_balance;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public LoanInfoEntity() {

    }




    public LoanInfoEntity(String loanName, String loanId, String loanImageUrl) {
        this.loanName = loanName;
        this.loanId = loanId;
        this.loanImageUrl = loanImageUrl;
    }


    public String getDateOfIncorporation() {
        return dateOfIncorporation;
    }

    public void setDateOfIncorporation(String dateOfIncorporation) {
        this.dateOfIncorporation = dateOfIncorporation;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getGrossTurnOver2() {
        return grossTurnOver2;
    }

    public void setGrossTurnOver2(String grossTurnOver2) {
        this.grossTurnOver2 = grossTurnOver2;
    }

    public String getGrossTurnOver3() {
        return grossTurnOver3;
    }

    public void setGrossTurnOver3(String grossTurnOver3) {
        this.grossTurnOver3 = grossTurnOver3;
    }

    public String getCheckBounced() {
        return checkBounced;
    }

    public void setCheckBounced(String checkBounced) {
        this.checkBounced = checkBounced;
    }

    public String getCompanyRatedByAgency() {
        return companyRatedByAgency;
    }

    public void setCompanyRatedByAgency(String companyRatedByAgency) {
        this.companyRatedByAgency = companyRatedByAgency;
    }

    public String getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(String officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public String getGrossTurnOverBeforeLastYear() {
        return grossTurnOverBeforeLastYear;
    }

    public void setGrossTurnOverBeforeLastYear(String grossTurnOverBeforeLastYear) {
        this.grossTurnOverBeforeLastYear = grossTurnOverBeforeLastYear;
    }

    public String getAuditDone() {
        return auditDone;
    }

    public void setAuditDone(String auditDone) {
        this.auditDone = auditDone;
    }

    public String getDefaultedOnLoan() {
        return defaultedOnLoan;
    }

    public void setDefaultedOnLoan(String defaultedOnLoan) {
        this.defaultedOnLoan = defaultedOnLoan;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public String getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(String totalExperience) {
        this.totalExperience = totalExperience;
    }

    public String getGrossTurnOver() {
        return grossTurnOver;
    }

    public void setGrossTurnOver(String grossTurnOver) {
        this.grossTurnOver = grossTurnOver;
    }

    public String getOfficeOwnerShip() {
        return officeOwnerShip;
    }

    public void setOfficeOwnerShip(String officeOwnerShip) {
        this.officeOwnerShip = officeOwnerShip;
    }

    public void setLoanImageUrl(String loanImageUrl) {
        this.loanImageUrl = loanImageUrl;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getSalaryProcess() {
        return salaryProcess;
    }

    public void setSalaryProcess(String salaryProcess) {
        this.salaryProcess = salaryProcess;
    }

    public String getEMI() {
        return EMI;
    }

    public void setEMI(String EMI) {
        this.EMI = EMI;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLoanImageUrl() {
        return loanImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getQualification() {
        return qualification;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getLoanName() {
        return loanName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getLoanApplyFor() {
        return loanApplyFor;
    }

    public void setLoanApplyFor(String loanApplyFor) {
        this.loanApplyFor = loanApplyFor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
