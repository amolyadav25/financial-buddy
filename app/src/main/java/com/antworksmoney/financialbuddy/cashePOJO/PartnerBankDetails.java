package com.antworksmoney.financialbuddy.cashePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerBankDetails {
    @SerializedName("Primary Existing Bank Name")
    @Expose
    private String primaryExistingBankName;
    @SerializedName("Account number")
    @Expose
    private String accountNumber;
    @SerializedName("IFSC Code")
    @Expose
    private String iFSCCode;
    @SerializedName("Remarks")
    @Expose
    private String remarks;

    public String getPrimaryExistingBankName() {
        return primaryExistingBankName;
    }

    public void setPrimaryExistingBankName(String primaryExistingBankName) {
        this.primaryExistingBankName = primaryExistingBankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIFSCCode() {
        return iFSCCode;
    }

    public void setIFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
