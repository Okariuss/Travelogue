package com.okada.travelogue.HelperClasses;

public class HotelClass  {
    private String name,imageUrl,city,address,roomPrice,roomCount,description,roomName;
    private double rate;
    private long entryMillis,leavingMillis;

    public HotelClass(String name, String imageUrl, String city, String address
            , String roomPrice, double rate,String roomCount,String description
            ,long entryMillis,long leavingMillis ,String roomName) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.city = city;
        this.address = address;
        this.roomPrice = roomPrice;
        this.rate = rate;
        this.roomCount=roomCount;
        this.description=description;
        this.entryMillis=entryMillis;
        this.leavingMillis=leavingMillis;
        this.roomName=roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public long getEntryMillis() {
        return entryMillis;
    }

    public long getLeavingMillis() {
        return leavingMillis;
    }

    public String getDescription() {
        return description;
    }

    public String getRoomCount() {
        return roomCount;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public double getRate() {
        return rate;
    }
}
