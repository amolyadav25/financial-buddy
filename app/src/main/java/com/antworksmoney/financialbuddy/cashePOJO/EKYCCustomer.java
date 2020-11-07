package com.antworksmoney.financialbuddy.cashePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EKYCCustomer {
    @SerializedName("poa")
    @Expose
    private Poa poa;
    @SerializedName("aadhar_no")
    @Expose
    private String aadharNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("compressed-address")
    @Expose
    private String compressedAddress;

    public Poa getPoa() {
        return poa;
    }

    public void setPoa(Poa poa) {
        this.poa = poa;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCompressedAddress() {
        return compressedAddress;
    }

    public void setCompressedAddress(String compressedAddress) {
        this.compressedAddress = compressedAddress;
    }
}
