package com.antworksmoney.financialbuddy.helpers.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class TrainingEntity implements Serializable {

    String id,Description,longDescription,Url;

    String thumbnail;

    String isSeen;

    ArrayList<String> categoryTags = new ArrayList<>();

    public ArrayList<String> getCategoryTags() {
        return categoryTags;
    }

    public void setCategoryTags(ArrayList<String> categoryTags) {
        this.categoryTags = categoryTags;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
