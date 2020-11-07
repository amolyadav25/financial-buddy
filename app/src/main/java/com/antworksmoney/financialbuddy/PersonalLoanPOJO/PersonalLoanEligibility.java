package com.antworksmoney.financialbuddy.PersonalLoanPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonalLoanEligibility {
    @SerializedName("is_declined")
    @Expose
    private Boolean isDeclined;
    @SerializedName("approved_limit")
    @Expose
    private Double approvedLimit;
    @SerializedName("details_required")
    @Expose
    private List<DetailsRequired> detailsRequired = null;
    @SerializedName("application")
    @Expose
    private Object application;

    public Boolean getIsDeclined() {
        return isDeclined;
    }

    public void setIsDeclined(Boolean isDeclined) {
        this.isDeclined = isDeclined;
    }

    public Double getApprovedLimit() {
        return approvedLimit;
    }

    public void setApprovedLimit(Double approvedLimit) {
        this.approvedLimit = approvedLimit;
    }

    public List<DetailsRequired> getDetailsRequired() {
        return detailsRequired;
    }

    public void setDetailsRequired(List<DetailsRequired> detailsRequired) {
        this.detailsRequired = detailsRequired;
    }

    public Object getApplication() {
        return application;
    }

    public void setApplication(Object application) {
        this.application = application;
    }
}
