package com.dto;

import com.documents.AppUser;
import com.documents.Authorization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.Claims;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Principal {


    private String username;
    private String _id;
    private String type;

    public Principal() { }

    public Principal(Claims jwtClaims) {
        this._id = jwtClaims.getId();
        this.username = jwtClaims.getSubject();
        this.type = jwtClaims.get("role", String.class);
    }
    public Principal(AppUser appUser)
    {
        System.out.println(appUser);
       this.username =  appUser.getUsername();
       this._id = appUser.getId();
       this.type = appUser.getAuthorization().toString();
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
