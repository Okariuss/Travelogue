package com.okada.travelogue.HelperClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name,surname,email,password,photoUrl;
    public int birthYear,birthMonth,birthDay;

    public User(){

    }

    public User(String name, String surname, String email, String password, int birthYear,int birthMonth,int birthDay,String photoUrl) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.photoUrl=photoUrl;
    }

}
