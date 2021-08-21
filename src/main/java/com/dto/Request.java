package com.dto;

public class Request {
    String name;
    String requesterName;
    String requesterCredentials;

    public Request(String name, String requesterName, String requesterCredentials) {
        this.name = name;
        this.requesterName = requesterName;
        this.requesterCredentials = requesterCredentials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterCredentials() {
        return requesterCredentials;
    }

    public void setRequesterCredentials(String requesterCredentials) {
        this.requesterCredentials = requesterCredentials;
    }
}
