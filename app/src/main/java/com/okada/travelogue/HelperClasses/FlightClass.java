package com.okada.travelogue.HelperClasses;


import com.google.firebase.Timestamp;



public class FlightClass {
    private String imageUrl,from,to,vehicle,companyName,detail, flightId;
    private Timestamp timeTakeOff,timeLanding;
    private int price;

    public FlightClass(String imageUrl, String from, String to, String vehicle,
                       Timestamp timeTakeOff, Timestamp timeLanding, int price,
                       String companyName, String detail, String flightId) {
        this.imageUrl = imageUrl;
        this.from = from;
        this.to = to;
        this.vehicle = vehicle;
        this.timeTakeOff = timeTakeOff;
        this.timeLanding = timeLanding;
        this.price = price;
        this.companyName=companyName;
        this.detail=detail;
        this.flightId =flightId;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getDetail() {
        return detail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getVehicle() {
        return vehicle;
    }

    public Timestamp getTimeTakeOff() {
        return timeTakeOff;
    }

    public Timestamp getTimeLanding() {
        return timeLanding;
    }

    public int getPrice() {
        return price;
    }


}
