package com.dto;

import com.documents.AppUser;
import com.documents.Authorization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SheildedUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Authorization authorization = Authorization.NONE;
    private LocalDateTime registrationTime;
    private List<String> registeredClasses = new ArrayList<String>();

    public SheildedUser(AppUser appUser)
    {
        this.id = appUser.getId();
        this.firstName = appUser.getFirstName();
        this.lastName = appUser.getLastName();
        this.email = appUser.getEmail();
        this.username = appUser.getUsername();
        this.registrationTime = appUser.getRegistrationTime();
        this.registeredClasses = appUser.getRegisteredClasses();
        this.authorization = appUser.getAuthorization();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
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
}
