package com.dto;

import com.documents.AppUser;
import com.documents.Authorization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Principal {


    private String username;
    private String _id;
    private String type;

    public Principal() { }

    public Principal(AppUser appUser)
    {
       this.username =  appUser.getUsername();
       this._id = appUser.getId();
       this.type = appUser.getAuthorization() == Authorization.STUDENT ? "STUDENT" :appUser.getAuthorization() == Authorization.NONE ? "NONE": "ADMINISTRATOR";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
