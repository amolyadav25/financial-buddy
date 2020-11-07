package com.antworksmoney.financialbuddy.cashePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CashePojoRequest {
    @SerializedName("partner_name")
    @Expose
    private String partnerName;
    @SerializedName("reference_Id")
    @Expose
    private String referenceId;
    @SerializedName("applicant_id")
    @Expose
    private String applicantId;
    @SerializedName("loan_amount")
    @Expose
    private String loanAmount;
    @SerializedName("product_type_name")
    @Expose
    private String productTypeName;
    @SerializedName("Personal Information")
    @Expose
    private PersonalInformation personalInformation;
    @SerializedName("Applicant Information")
    @Expose
    private ApplicantInformation applicantInformation;
    @SerializedName("Financial Information")
    @Expose
    private FinancialInformation financialInformation;
    @SerializedName("Partner Bank Details")
    @Expose
    private PartnerBankDetails partnerBankDetails;
    @SerializedName("Contact Information")
    @Expose
    private ContactInformation contactInformation;
    @SerializedName("e-KYC Customer")
    @Expose
    private EKYCCustomer eKYCCustomer;

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(PersonalInformation personalInformation) {
        this.personalInformation = personalInformation;
    }

    public ApplicantInformation getApplicantInformation() {
        return applicantInformation;
    }

    public void setApplicantInformation(ApplicantInformation applicantInformation) {
        this.applicantInformation = applicantInformation;
    }

    public FinancialInformation getFinancialInformation() {
        return financialInformation;
    }

    public void setFinancialInformation(FinancialInformation financialInformation) {
        this.financialInformation = financialInformation;
    }

    public PartnerBankDetails getPartnerBankDetails() {
        return partnerBankDetails;
    }

    public void setPartnerBankDetails(PartnerBankDetails partnerBankDetails) {
        this.partnerBankDetails = partnerBankDetails;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public EKYCCustomer getEKYCCustomer() {
        return eKYCCustomer;
    }

    public void setEKYCCustomer(EKYCCustomer eKYCCustomer) {
        this.eKYCCustomer = eKYCCustomer;
    }

}
