package com.antworksmoney.financialbuddy.helpers.Entity;

import android.graphics.Bitmap;


import java.io.Serializable;

public class ProfileInfo implements Serializable {

    public String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String bloodgroup;
    private String contact_donor_name;
    private String contact_donor_number;
    private String dateOfBirth;
    private String medicalConditions;
    private String allergiesAndReaction;
    private String heightofperson;
    private String weightofperson;
    private String familyDoctorName;
    private String volanteer;

    private Bitmap image;

    private String imageUrl;

    private String token;

    public String getImageUrl() {
        return imageUrl;
    }


    public String getToken() {
        return token;
    }

    public ProfileInfo(String name,
                       String email,
                       String phoneNumber,
                       String gender,
                       String bloodgroup,
                       String contact_donor_name,
                       String contact_donor_number,
                       String dateOfBirth,
                       String medicalConditions,
                       String allergiesAndReaction,
                       String heightofperson,
                       String weightofperson,
                       String familyDoctorName,
                       String volanteer,
                       Bitmap image) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.contact_donor_name = contact_donor_name;
        this.contact_donor_number = contact_donor_number;
        this.dateOfBirth = dateOfBirth;
        this.medicalConditions = medicalConditions;
        this.allergiesAndReaction = allergiesAndReaction;
        this.heightofperson = heightofperson;
        this.weightofperson = weightofperson;
        this.familyDoctorName = familyDoctorName;
        this.volanteer = volanteer;
        this.image = image;
    }


    public ProfileInfo(String name, String number, String mImage, String token){
        this.name = name;
        this.contact_donor_number = number;
//        this.mLocation = location;
        this.imageUrl = mImage;
        this.token = token;

    }


    public ProfileInfo(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getContact_donor_name() {
        return contact_donor_name;
    }

    public String getContact_donor_number() {
        return contact_donor_number;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public String getAllergiesAndReaction() {
        return allergiesAndReaction;
    }

    public String getHeightofperson() {
        return heightofperson;
    }

    public String getWeightofperson() {
        return weightofperson;
    }

    public String getFamilyDoctorName() {
        return familyDoctorName;
    }

    public String getVolanteer() {
        return volanteer;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", bloodgroup='" + bloodgroup + '\'' +
                ", contact_donor_name='" + contact_donor_name + '\'' +
                ", contact_donor_number='" + contact_donor_number + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", medicalConditions='" + medicalConditions + '\'' +
                ", allergiesAndReaction='" + allergiesAndReaction + '\'' +
                ", heightofperson='" + heightofperson + '\'' +
                ", weightofperson='" + weightofperson + '\'' +
                ", familyDoctorName='" + familyDoctorName + '\'' +
                ", volanteer='" + volanteer + '\'' +
                ", image=" + image +
                ", imageUrl='" + imageUrl + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
