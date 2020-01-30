package com.antworksmoney.financialbuddy.helpers.Entity;

import java.io.Serializable;

public class BillerEntity implements Serializable {

//     "billerId": "DISH00000NAT01",
//             "billerName": "DishTV",
//             "billerCategory": "DTH",
//             "billerAdhoc": "true",
//             "billerCoverage": "INDIA",
//             "billerFetchRequiremet": "NOT_SUPPORTED",
//             "billerPaymentExactness": "EXACT",
//             "billerSupportBillValidation": "OPTIONAL",
//             "billerInputParams": {
//        "paramInfo": {
//            "paramName": "Registered Mobile Number / Viewing Card Number",
//                    "dataType": "NUMERIC",
//                    "isOptional": "false",
//                    "minLength": "0",
//                    "maxLength": "0"
//        }
//    },
//            "billerAmountOptions": "BASE_BILL_AMOUNT",
//            "billerPaymentModes": "DEBIT CARD, CREDIT CARD, WALLET, AEPS, INTERNET BANKING, NEFT, UPI, CASH, IMPS, PREPAID CARD",
//            "billerDescription": "test",
//            "rechargeAmountInValidationRequest": "NOT_SUPPORTED"

    String billerId,
            billerName,
            billerCategory,
            billerAdhoc,
            billerCoverage,
            billerFetchRequiremet,
            billerPaymentExactness,
            billerSupportBillValidation,
            billerAmountOptions,
            billerPaymentModes,
            billerDescription,
            rechargeAmountInValidationRequest,
            orderId;

    int drawable;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public String getBillerCategory() {
        return billerCategory;
    }

    public void setBillerCategory(String billerCategory) {
        this.billerCategory = billerCategory;
    }

    public String getBillerAdhoc() {
        return billerAdhoc;
    }

    public void setBillerAdhoc(String billerAdhoc) {
        this.billerAdhoc = billerAdhoc;
    }

    public String getBillerCoverage() {
        return billerCoverage;
    }

    public void setBillerCoverage(String billerCoverage) {
        this.billerCoverage = billerCoverage;
    }

    public String getBillerFetchRequiremet() {
        return billerFetchRequiremet;
    }

    public void setBillerFetchRequiremet(String billerFetchRequiremet) {
        this.billerFetchRequiremet = billerFetchRequiremet;
    }

    public String getBillerPaymentExactness() {
        return billerPaymentExactness;
    }

    public void setBillerPaymentExactness(String billerPaymentExactness) {
        this.billerPaymentExactness = billerPaymentExactness;
    }

    public String getBillerSupportBillValidation() {
        return billerSupportBillValidation;
    }

    public void setBillerSupportBillValidation(String billerSupportBillValidation) {
        this.billerSupportBillValidation = billerSupportBillValidation;
    }

    public String getBillerAmountOptions() {
        return billerAmountOptions;
    }

    public void setBillerAmountOptions(String billerAmountOptions) {
        this.billerAmountOptions = billerAmountOptions;
    }

    public String getBillerPaymentModes() {
        return billerPaymentModes;
    }

    public void setBillerPaymentModes(String billerPaymentModes) {
        this.billerPaymentModes = billerPaymentModes;
    }

    public String getBillerDescription() {
        return billerDescription;
    }

    public void setBillerDescription(String billerDescription) {
        this.billerDescription = billerDescription;
    }

    public String getRechargeAmountInValidationRequest() {
        return rechargeAmountInValidationRequest;
    }

    public void setRechargeAmountInValidationRequest(String rechargeAmountInValidationRequest) {
        this.rechargeAmountInValidationRequest = rechargeAmountInValidationRequest;
    }
}
