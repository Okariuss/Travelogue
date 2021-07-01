package com.okada.travelogue.HelperClasses;

public class CityClass {
    private String imageUrl,name, description,wikipediaUrl;
    private float rate;


    public CityClass(String imageUrl, float rate, String name, String description,String wikipediaUrl) {
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.name = name;
        this.description = description;
        this.wikipediaUrl=wikipediaUrl;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
