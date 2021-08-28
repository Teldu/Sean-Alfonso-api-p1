package com.dto;

import com.documents.AppUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SheildedUser {
    private String _id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String type;
    private LocalDateTime registrationTime;
    private List<String> registeredClasses = new ArrayList<String>();

    public SheildedUser(AppUser appUser)
    {
        this._id = appUser.getId();
        this.firstName = appUser.getFirstName();
        this.lastName = appUser.getLastName();
        this.email = appUser.getEmail();
        this.username = appUser.getUsername();
        this.registrationTime = appUser.getRegistrationTime();
        this.registeredClasses = appUser.getRegisteredClasses();
        this.type = appUser.getAuthorization().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public List<String> getRegisteredClasses() {
        return registeredClasses;
    }

    public void setRegisteredClasses(List<String> registeredClasses) {
        this.registeredClasses = registeredClasses;
    }

    @Override
    public String toString() {
        return "SheildedUser{" +
                "_id='" + _id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", registrationTime=" + registrationTime +
                ", registeredClasses=" + registeredClasses +
                '}';
    }
}
