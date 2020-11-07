package com.antworksmoney.financialbuddy.checksumPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChecksumPOJO {
    @SerializedName("check_sum")
    @Expose
    private String checkSum;

    public String getCheckSum() {
        return checkSum;
    }
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
