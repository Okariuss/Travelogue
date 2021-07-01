package com.okada.travelogue.HelperClasses;

public class TicketClass {
    private String  flightId, passDob, passDocCountry,
            passDocNo, passGender, passName, passSurname, seat, ticketPurchTime;

    public TicketClass( String flightId, String passDob, String passDocCountry,
                       String passDocNo, String passGender, String passName,String passSurname, String seat,
                       String ticketPurchTime) {
        this.flightId = flightId;
        this.passDob = passDob;
        this.passDocCountry = passDocCountry;
        this.passDocNo = passDocNo;
        this.passGender = passGender;
        this.passName = passName;
        this.seat = seat;
        this.ticketPurchTime = ticketPurchTime;
        this.passSurname=passSurname;
    }

    public String getPassSurname() {
        return passSurname;
    }

    public void setPassSurname(String passSurname) {
        this.passSurname = passSurname;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getPassDob() {
        return passDob;
    }

    public void setPassDob(String passDob) {
        this.passDob = passDob;
    }

    public String getPassDocCountry() {
        return passDocCountry;
    }

    public void setPassDocCountry(String passDocCountry) {
        this.passDocCountry = passDocCountry;
    }

    public String getPassDocNo() {
        return passDocNo;
    }

    public void setPassDocNo(String passDocNo) {
        this.passDocNo = passDocNo;
    }

    public String getPassGender() {
        return passGender;
    }

    public void setPassGender(String passGender) {
        this.passGender = passGender;
    }

    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTicketPurchTime() {
        return ticketPurchTime;
    }

    public void setTicketPurchTime(String ticketPurchTime) {
        this.ticketPurchTime = ticketPurchTime;
    }
}
