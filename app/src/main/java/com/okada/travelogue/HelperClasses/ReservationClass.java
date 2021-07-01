package com.okada.travelogue.HelperClasses;

public class ReservationClass {
    String customerContact,startTime,endTime,hotelId;
    int price;

    public ReservationClass(String customerContact, String startTime, String endTime, String hotelId, int price) {
        this.customerContact = customerContact;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hotelId = hotelId;
        this.price = price;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getHotelId() {
        return hotelId;
    }

    public int getPrice() {
        return price;
    }
}
