package com.dto;

import com.documents.AppUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SheildedUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
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
    }
}
