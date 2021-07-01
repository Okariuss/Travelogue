package com.okada.travelogue.HelperClasses;

public class CompanyClass {
    private String imageUrl;
    private String name, description,wikipediaUrl;

    public CompanyClass(String imageUrl,String name, String description,String wikipediaUrl) {
        this.imageUrl = imageUrl;
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

    public void setImageUrl(String image) {
        this.imageUrl = image;
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