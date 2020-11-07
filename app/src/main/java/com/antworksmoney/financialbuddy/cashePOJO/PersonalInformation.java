package com.antworksmoney.financialbuddy.cashePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalInformation {
    @SerializedName("First Name")
    @Expose
    private String firstName;
    @SerializedName("Last Name")
    @Expose
    private String lastName;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("Address Line 1")
    @Expose
    private String addressLine1;
    @SerializedName("Address Line 2")
    @Expose
    private String addressLine2;
    @SerializedName("Landmark (Address Line 3)")
    @Expose
    private String landmarkAddressLine3;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Type of Accommodation")
    @Expose
    private String typeOfAccommodation;
    @SerializedName("PAN")
    @Expose
    private String pAN;
    @SerializedName("Aadhaar")
    @Expose
    private String aadhaar;
    @SerializedName("Highest Qualification")
    @Expose
    private String highestQualification;
    @SerializedName("residing_with")
    @Expose
    private String residingWith;
    @SerializedName("number_of_years_at_current_address")
    @Expose
    private String numberOfYearsAtCurrentAddress;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("spouse_employment_status")
    @Expose
    private String spouseEmploymentStatus;
    @SerializedName("number_of_kids")
    @Expose
    private String numberOfKids;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmarkAddressLine3() {
        return landmarkAddressLine3;
    }

    public void setLandmarkAddressLine3(String landmarkAddressLine3) {
        this.landmarkAddressLine3 = landmarkAddressLine3;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTypeOfAccommodation() {
        return typeOfAccommodation;
    }

    public void setTypeOfAccommodation(String typeOfAccommodation) {
        this.typeOfAccommodation = typeOfAccommodation;
    }

    public String getPAN() {
        return pAN;
    }

    public void setPAN(String pAN) {
        this.pAN = pAN;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getHighestQualification() {
        return highestQualification;
    }

    public void setHighestQualification(String highestQualification) {
        this.highestQualification = highestQualification;
    }

    public String getResidingWith() {
        return residingWith;
    }

    public void setResidingWith(String residingWith) {
        this.residingWith = residingWith;
    }

    public String getNumberOfYearsAtCurrentAddress() {
        return numberOfYearsAtCurrentAddress;
    }

    public void setNumberOfYearsAtCurrentAddress(String numberOfYearsAtCurrentAddress) {
        this.numberOfYearsAtCurrentAddress = numberOfYearsAtCurrentAddress;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouseEmploymentStatus() {
        return spouseEmploymentStatus;
    }

    public void setSpouseEmploymentStatus(String spouseEmploymentStatus) {
        this.spouseEmploymentStatus = spouseEmploymentStatus;
    }

    public String getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(String numberOfKids) {
        this.numberOfKids = numberOfKids;
    }
}
