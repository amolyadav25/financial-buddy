package com.antworksmoney.financialbuddy.helpers.Entity;

import java.io.Serializable;

public class RegionalDataInfo implements Serializable {

    public String Name;
    public String id;
    public String code;

    public RegionalDataInfo(String id, String name, String code) {
        Name = name;
        this.id = id;
        this.code = code;
    }

    public RegionalDataInfo(String name, String code) {
        Name = name;
        this.code = code;
    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
