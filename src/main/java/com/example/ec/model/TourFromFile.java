package com.example.ec.model;

import com.example.ec.domain.Difficulties;
import com.example.ec.domain.Region;

public class TourFromFile {

    private String packageType;
    private String title;
    private String blurb;
    private String description;
    private String bullets;
    private String difficulty;
    private String length;
    private String price;
    private String region;
    private String keywords;

    public String getPackageType() {
        return packageType;
    }

    public String getTitle() {
        return title;
    }

    public String getBlurb() {
        return blurb;
    }

    public String getDescription() {
        return description;
    }

    public String getBullets() {
        return bullets;
    }

    public Difficulties getDifficulty() {
        return Difficulties.valueOf(difficulty);
    }

    public String getLength() {
        return length;
    }

    public Integer getPrice() {
        return Integer.parseInt(price);
    }

    public Region getRegion() {
        return Region.findByLabel(region);
    }

    public String getKeywords() {
        return keywords;
    }
}
