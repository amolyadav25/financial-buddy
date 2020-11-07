package com.antworksmoney.financialbuddy.cashePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinancialInformation {
    @SerializedName("Primary Existing Bank Name")
    @Expose
    private String primaryExistingBankName;
    @SerializedName("Account number")
    @Expose
    private String accountNumber;
    @SerializedName("IFSC Code")
    @Expose
    private String iFSCCode;

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
}
