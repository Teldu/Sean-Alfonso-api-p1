package com.dto.RequestObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
    String name;
    String request;



    public Request(){}
    public Request(String name, String requesterName, String requesterCredentials) {
        this.name = name;
        this.request = requesterName;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }


}
