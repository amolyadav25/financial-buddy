package com.antworksmoney.financialbuddy.helpers.Entity;

import java.io.Serializable;

public class AppInfoEntity implements Serializable {

    String app_id,
            app_name,
            app_url,
            app_logo_url,
            app_image_url,
            app_company_name,
            app_details,
            app_category,
            app_rating,
            app_downloads;


    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getApp_logo_url() {
        return app_logo_url;
    }

    public void setApp_logo_url(String app_logo_url) {
        this.app_logo_url = app_logo_url;
    }

    public String getApp_image_url() {
        return app_image_url;
    }

    public void setApp_image_url(String app_image_url) {
        this.app_image_url = app_image_url;
    }

    public String getApp_company_name() {
        return app_company_name;
    }

    public void setApp_company_name(String app_company_name) {
        this.app_company_name = app_company_name;
    }

    public String getApp_details() {
        return app_details;
    }

    public void setApp_details(String app_details) {
        this.app_details = app_details;
    }

    public String getApp_category() {
        return app_category;
    }

    public void setApp_category(String app_category) {
        this.app_category = app_category;
    }

    public String getApp_rating() {
        return app_rating;
    }

    public void setApp_rating(String app_rating) {
        this.app_rating = app_rating;
    }

    public String getApp_downloads() {
        return app_downloads;
    }

    public void setApp_downloads(String app_downloads) {
        this.app_downloads = app_downloads;
    }
}
