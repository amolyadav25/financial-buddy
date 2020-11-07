package com.antworksmoney.financialbuddy.paysenseupdatePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaySenseUpdateRequest {
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("approved_limit")
    @Expose
    private String approvedLimit;
    @SerializedName("is_declined")
    @Expose
    private String isDeclined;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getApprovedLimit() {
        return approvedLimit;
    }

    public void setApprovedLimit(String approvedLimit) {
        this.approvedLimit = approvedLimit;
    }

    public String getIsDeclined() {
        return isDeclined;
    }

    public void setIsDeclined(String isDeclined) {
        this.isDeclined = isDeclined;
    }

}
