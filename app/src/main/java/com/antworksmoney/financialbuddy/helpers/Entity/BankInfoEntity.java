package com.antworksmoney.financialbuddy.helpers.Entity;

import java.io.Serializable;

public class BankInfoEntity implements Serializable {

    String loanid,
            bank_id,
            min_age,
            max_age,
            gender,
            employement_status,
            min_salary,
            min_average_income,
            min_loan_amount,
            max_loan_amount,
            processing_fee_type,
            fixed_processing_fee,
            min_processing_fee,
            max_processing_fee,
            inr_rate_fixed,
            int_fixed_amount,
            min_int_rate,
            max_int_rate,
            tenure_month_start,
            tenure_month_end,
            minimum_cibil,
            min_residence_yrs,
            min_employement_yrs,
            nationality,
            company_id,
            loan_type_id,
            credit_card_type,
            credit_card_image,
            loan_description,
            f1,f2,f3,f4,f5,status,
            date_added,
            date_modified,
            for_crm,
            name,image,
            about,added_on;


    public BankInfoEntity() {

    }

    public BankInfoEntity(String loanid,
                          String bank_id,
                          String min_age,
                          String max_age,
                          String gender,
                          String employement_status,
                          String min_salary,
                          String min_average_income,
                          String min_loan_amount,
                          String max_loan_amount,
                          String processing_fee_type,
                          String fixed_processing_fee,
                          String min_processing_fee,
                          String max_processing_fee,
                          String inr_rate_fixed,
                          String int_fixed_amount,
                          String min_int_rate,
                          String max_int_rate,
                          String tenure_month_start,
                          String tenure_month_end,
                          String minimum_cibil,
                          String min_residence_yrs,
                          String min_employement_yrs,
                          String nationality,
                          String company_id,
                          String loan_type_id,
                          String credit_card_type,
                          String credit_card_image,
                          String loan_description,
                          String f1,
                          String f2,
                          String f3,
                          String f4,
                          String f5,
                          String status,
                          String date_added,
                          String date_modified,
                          String for_crm,
                          String name,
                          String image,
                          String about,
                          String added_on) {
        this.loanid = loanid;
        this.bank_id = bank_id;
        this.min_age = min_age;
        this.max_age = max_age;
        this.gender = gender;
        this.employement_status = employement_status;
        this.min_salary = min_salary;
        this.min_average_income = min_average_income;
        this.min_loan_amount = min_loan_amount;
        this.max_loan_amount = max_loan_amount;
        this.processing_fee_type = processing_fee_type;
        this.fixed_processing_fee = fixed_processing_fee;
        this.min_processing_fee = min_processing_fee;
        this.max_processing_fee = max_processing_fee;
        this.inr_rate_fixed = inr_rate_fixed;
        this.int_fixed_amount = int_fixed_amount;
        this.min_int_rate = min_int_rate;
        this.max_int_rate = max_int_rate;
        this.tenure_month_start = tenure_month_start;
        this.tenure_month_end = tenure_month_end;
        this.minimum_cibil = minimum_cibil;
        this.min_residence_yrs = min_residence_yrs;
        this.min_employement_yrs = min_employement_yrs;
        this.nationality = nationality;
        this.company_id = company_id;
        this.loan_type_id = loan_type_id;
        this.credit_card_type = credit_card_type;
        this.credit_card_image = credit_card_image;
        this.loan_description = loan_description;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        this.status = status;
        this.date_added = date_added;
        this.date_modified = date_modified;
        this.for_crm = for_crm;
        this.name = name;
        this.image = image;
        this.about = about;
        this.added_on = added_on;
    }


    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public void setMin_age(String min_age) {
        this.min_age = min_age;
    }

    public void setMax_age(String max_age) {
        this.max_age = max_age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmployement_status(String employement_status) {
        this.employement_status = employement_status;
    }

    public void setMin_salary(String min_salary) {
        this.min_salary = min_salary;
    }

    public void setMin_average_income(String min_average_income) {
        this.min_average_income = min_average_income;
    }

    public void setMin_loan_amount(String min_loan_amount) {
        this.min_loan_amount = min_loan_amount;
    }

    public void setMax_loan_amount(String max_loan_amount) {
        this.max_loan_amount = max_loan_amount;
    }

    public void setProcessing_fee_type(String processing_fee_type) {
        this.processing_fee_type = processing_fee_type;
    }

    public void setFixed_processing_fee(String fixed_processing_fee) {
        this.fixed_processing_fee = fixed_processing_fee;
    }

    public void setMin_processing_fee(String min_processing_fee) {
        this.min_processing_fee = min_processing_fee;
    }

    public void setMax_processing_fee(String max_processing_fee) {
        this.max_processing_fee = max_processing_fee;
    }

    public void setInr_rate_fixed(String inr_rate_fixed) {
        this.inr_rate_fixed = inr_rate_fixed;
    }

    public void setInt_fixed_amount(String int_fixed_amount) {
        this.int_fixed_amount = int_fixed_amount;
    }

    public void setMin_int_rate(String min_int_rate) {
        this.min_int_rate = min_int_rate;
    }

    public void setMax_int_rate(String max_int_rate) {
        this.max_int_rate = max_int_rate;
    }

    public void setTenure_month_start(String tenure_month_start) {
        this.tenure_month_start = tenure_month_start;
    }

    public void setTenure_month_end(String tenure_month_end) {
        this.tenure_month_end = tenure_month_end;
    }

    public void setMinimum_cibil(String minimum_cibil) {
        this.minimum_cibil = minimum_cibil;
    }

    public void setMin_residence_yrs(String min_residence_yrs) {
        this.min_residence_yrs = min_residence_yrs;
    }

    public void setMin_employement_yrs(String min_employement_yrs) {
        this.min_employement_yrs = min_employement_yrs;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public void setLoan_type_id(String loan_type_id) {
        this.loan_type_id = loan_type_id;
    }

    public void setCredit_card_type(String credit_card_type) {
        this.credit_card_type = credit_card_type;
    }

    public void setCredit_card_image(String credit_card_image) {
        this.credit_card_image = credit_card_image;
    }

    public void setLoan_description(String loan_description) {
        this.loan_description = loan_description;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public void setF2(String f2) {
        this.f2 = f2;
    }

    public void setF3(String f3) {
        this.f3 = f3;
    }

    public void setF4(String f4) {
        this.f4 = f4;
    }

    public void setF5(String f5) {
        this.f5 = f5;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public void setFor_crm(String for_crm) {
        this.for_crm = for_crm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setAdded_on(String added_on) {
        this.added_on = added_on;
    }

    public String getLoanid() {
        return loanid;
    }

    public String getBank_id() {
        return bank_id;
    }

    public String getMin_age() {
        return min_age;
    }

    public String getMax_age() {
        return max_age;
    }

    public String getGender() {
        return gender;
    }

    public String getEmployement_status() {
        return employement_status;
    }

    public String getMin_salary() {
        return min_salary;
    }

    public String getMin_average_income() {
        return min_average_income;
    }

    public String getMin_loan_amount() {
        return min_loan_amount;
    }

    public String getMax_loan_amount() {
        return max_loan_amount;
    }

    public String getProcessing_fee_type() {
        return processing_fee_type;
    }

    public String getFixed_processing_fee() {
        return fixed_processing_fee;
    }

    public String getMin_processing_fee() {
        return min_processing_fee;
    }

    public String getMax_processing_fee() {
        return max_processing_fee;
    }

    public String getInr_rate_fixed() {
        return inr_rate_fixed;
    }

    public String getInt_fixed_amount() {
        return int_fixed_amount;
    }

    public String getMin_int_rate() {
        return min_int_rate;
    }

    public String getMax_int_rate() {
        return max_int_rate;
    }

    public String getTenure_month_start() {
        return tenure_month_start;
    }

    public String getTenure_month_end() {
        return tenure_month_end;
    }

    public String getMinimum_cibil() {
        return minimum_cibil;
    }

    public String getMin_residence_yrs() {
        return min_residence_yrs;
    }

    public String getMin_employement_yrs() {
        return min_employement_yrs;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getLoan_type_id() {
        return loan_type_id;
    }

    public String getCredit_card_type() {
        return credit_card_type;
    }

    public String getCredit_card_image() {
        return credit_card_image;
    }

    public String getLoan_description() {
        return loan_description;
    }

    public String getF1() {
        return f1;
    }

    public String getF2() {
        return f2;
    }

    public String getF3() {
        return f3;
    }

    public String getF4() {
        return f4;
    }

    public String getF5() {
        return f5;
    }

    public String getStatus() {
        return status;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public String getFor_crm() {
        return for_crm;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getAbout() {
        return about;
    }

    public String getAdded_on() {
        return added_on;
    }
}
