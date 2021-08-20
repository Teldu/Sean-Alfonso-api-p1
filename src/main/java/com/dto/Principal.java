package com.dto;

import com.documents.AppUser;
import com.documents.Authorization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Principal {


    private String username;
    private String id;
    private String type;

    public Principal() { }

    public Principal(AppUser appUser)
    {
       this.username =  appUser.getUsername();
       this.id = appUser.getId();
       this.type = appUser.getAuthorization() == Authorization.STUDENT ? "STUDENT" : "ADMINISTRATOR";
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
