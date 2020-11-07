package com.antworksmoney.financialbuddy.updateTokenPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class uploadTokenRequest {
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("token")
    @Expose
    private String token;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
