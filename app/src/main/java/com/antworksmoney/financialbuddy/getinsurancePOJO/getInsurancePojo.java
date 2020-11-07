package com.antworksmoney.financialbuddy.getinsurancePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getInsurancePojo {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("pdf_url")
    @Expose
    private String pdfUrl;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
