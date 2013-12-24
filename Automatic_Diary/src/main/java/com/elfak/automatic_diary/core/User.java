package com.elfak.automatic_diary.core;

import java.io.Serializable;

/**
 * Created by dusanristic on 12/16/13.
 */
public class User implements Serializable {

    protected String firstName;
    protected String lastName;
    protected String username;
    protected String sessionToken;
    protected String currentCountry;
    protected String currentCity;
    protected String image;
    protected String  bday;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return image;
    }

    public String getBday() {
        return bday;
    }

    public void setBirthDay(String bday){
        this.bday = bday;
    }
}
