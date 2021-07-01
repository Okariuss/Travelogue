package com.okada.travelogue.HelperClasses;

import java.util.HashMap;

public class HelperClass {
    public static HashMap<Integer,HashMap<String,String>> map=null;
    public static String[] selectedSeats;
    public static String busySeats;
    public static boolean isTrue;

    public static HashMap<Integer, HashMap<String, String>> getMap() {
        return map;
    }

    public static void setMap(HashMap<Integer, HashMap<String, String>> map) {
        HelperClass.map = map;
    }

    public static boolean isIsTrue() {
        return isTrue;
    }

    public static void setIsTrue(boolean isTrue) {
        HelperClass.isTrue = isTrue;
    }

    public static String getBusySeats() {
        return busySeats;
    }

    public static void setBusySeats(String busySeats) {
        HelperClass.busySeats = busySeats;
    }

    public static String[] getSelectedSeats(int passengerCount) {
        if (selectedSeats==null){
            return new String[passengerCount];
        }return selectedSeats;
    }

    public static void setSelectedSeats(String[] selectedSeats) {
        HelperClass.selectedSeats = selectedSeats;
    }
}
