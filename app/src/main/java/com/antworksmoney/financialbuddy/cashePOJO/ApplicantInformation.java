package com.antworksmoney.financialbuddy.cashePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicantInformation {
    @SerializedName("Company Name")
    @Expose
    private String companyName;
    @SerializedName("Office Phone no")
    @Expose
    private String officePhoneNo;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("Monthly Income")
    @Expose
    private String monthlyIncome;
    @SerializedName("Number of Years in Current Work")
    @Expose
    private String numberOfYearsInCurrentWork;
    @SerializedName("Official Email")
    @Expose
    private String officialEmail;
    @SerializedName("Office Address 1")
    @Expose
    private String officeAddress1;
    @SerializedName("Office Address 2")
    @Expose
    private String officeAddress2;
    @SerializedName("Landmark (Office)")
    @Expose
    private String landmarkOffice;
    @SerializedName("Office Pincode")
    @Expose
    private String officePincode;
    @SerializedName("Office City")
    @Expose
    private String officeCity;
    @SerializedName("Office State")
    @Expose
    private String officeState;
    @SerializedName("Working Since")
    @Expose
    private String workingSince;
    @SerializedName("Employment Type")
    @Expose
    private String employmentType;
    @SerializedName("Salary ReceivedTypeId")
    @Expose
    private String salaryReceivedTypeId;
    @SerializedName("work_sector")
    @Expose
    private String workSector;
    @SerializedName("job_function")
    @Expose
    private String jobFunction;
    @SerializedName("organization")
    @Expose
    private String organization;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfficePhoneNo() {
        return officePhoneNo;
    }

    public void setOfficePhoneNo(String officePhoneNo) {
        this.officePhoneNo = officePhoneNo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getNumberOfYearsInCurrentWork() {
        return numberOfYearsInCurrentWork;
    }

    public void setNumberOfYearsInCurrentWork(String numberOfYearsInCurrentWork) {
        this.numberOfYearsInCurrentWork = numberOfYearsInCurrentWork;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficeAddress1() {
        return officeAddress1;
    }

    public void setOfficeAddress1(String officeAddress1) {
        this.officeAddress1 = officeAddress1;
    }

    public String getOfficeAddress2() {
        return officeAddress2;
    }

    public void setOfficeAddress2(String officeAddress2) {
        this.officeAddress2 = officeAddress2;
    }

    public String getLandmarkOffice() {
        return landmarkOffice;
    }

    public void setLandmarkOffice(String landmarkOffice) {
        this.landmarkOffice = landmarkOffice;
    }

    public String getOfficePincode() {
        return officePincode;
    }

    public void setOfficePincode(String officePincode) {
        this.officePincode = officePincode;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeState() {
        return officeState;
    }

    public void setOfficeState(String officeState) {
        this.officeState = officeState;
    }

    public String getWorkingSince() {
        return workingSince;
    }

    public void setWorkingSince(String workingSince) {
        this.workingSince = workingSince;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getSalaryReceivedTypeId() {
        return salaryReceivedTypeId;
    }

    public void setSalaryReceivedTypeId(String salaryReceivedTypeId) {
        this.salaryReceivedTypeId = salaryReceivedTypeId;
    }

    public String getWorkSector() {
        return workSector;
    }

    public void setWorkSector(String workSector) {
        this.workSector = workSector;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

}
