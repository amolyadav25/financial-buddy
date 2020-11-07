package com.antworksmoney.financialbuddy.cashestatusupdatePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CasheStatusUpdateRequest {
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
