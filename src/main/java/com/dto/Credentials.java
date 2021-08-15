package com.dto;

public class Credentials {

    private String username;
    private String password;
    private String type;

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
