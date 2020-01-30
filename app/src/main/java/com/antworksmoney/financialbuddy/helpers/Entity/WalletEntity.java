package com.antworksmoney.financialbuddy.helpers.Entity;

public class WalletEntity {

    private String id;
    private String payoutId;
    private String leadId;
    private String amount;
    private String disburseLoanAmount;
    private String payoutPercentage;
    private String payoutType;
    private String approved;
    private String status;
    private String createdDate;
    private String borrowerName;
    private String loanType;
    private String loanAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(String payoutId) {
        this.payoutId = payoutId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDisburseLoanAmount() {
        return disburseLoanAmount;
    }

    public void setDisburseLoanAmount(String disburseLoanAmount) {
        this.disburseLoanAmount = disburseLoanAmount;
    }

    public String getPayoutPercentage() {
        return payoutPercentage;
    }

    public void setPayoutPercentage(String payoutPercentage) {
        this.payoutPercentage = payoutPercentage;
    }

    public String getPayoutType() {
        return payoutType;
    }

    public void setPayoutType(String payoutType) {
        this.payoutType = payoutType;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }
}
