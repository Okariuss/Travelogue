package com.okada.travelogue.HelperClasses;

public class AnnouncementClass {
    private String title,description;
    public AnnouncementClass(String title,String description) {
        this.title = title;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
