package com.antworksmoney.financialbuddy.PersonalLoanPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalLoanSuccessPOJO {
    @SerializedName("session-token")
    @Expose
    private String sessionToken;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

}
