package com.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Credentials {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String type;

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

    public String getUsername() {
        return username;
    }

    public Credentials(){ }
    public Credentials(String username ,String password , String type)
    {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getUserName() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }
}
