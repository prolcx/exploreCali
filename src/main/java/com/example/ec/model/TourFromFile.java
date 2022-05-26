package com.example.ec.model;


import java.util.Map;

public class TourFromFile {

    private String title;
    private String packageName;
    private Map<String, String> details;

    public TourFromFile(Map<String, String> record) {
        this.title = record.get("title");
        this.packageName = record.get("packageType");
        this.details = record;
        this.details.remove("packageType");
        this.details.remove("title");
    }

    public String getTitle() {
        return title;
    }

    public String getPackageName() {
        return packageName;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
