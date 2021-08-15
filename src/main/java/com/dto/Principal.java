package com.dto;

import com.documents.AppUser;

public class Principal {


    private String username;
    private String id;
    private String type;

    public Principal(AppUser appUser)
    {
       this.username =  appUser.getUsername();
       this.id = appUser.getId();

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
