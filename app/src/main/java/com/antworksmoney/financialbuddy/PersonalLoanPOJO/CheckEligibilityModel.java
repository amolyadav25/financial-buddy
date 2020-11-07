package com.antworksmoney.financialbuddy.PersonalLoanPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckEligibilityModel {
    @SerializedName("details")
    @Expose
    private Object details;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
