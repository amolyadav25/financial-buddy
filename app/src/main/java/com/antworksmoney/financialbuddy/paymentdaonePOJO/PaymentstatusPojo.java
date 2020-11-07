package com.antworksmoney.financialbuddy.paymentdaonePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentstatusPojo {
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("surName")
    @Expose
    private String surName;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
