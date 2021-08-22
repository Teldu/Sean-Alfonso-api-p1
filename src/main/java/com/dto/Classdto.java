package com.dto;

import com.documents.AppUser;
import com.documents.Authorization;
import com.documents.ClassDetails;
import com.documents.MeetingPeriods;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Classdto {

    private String className;
    private int classSize;
    private boolean open = false;
    private LocalDateTime registrationTime;
    private LocalDateTime registrationClosedTime;
    private String meetingPeriod;
    private List<String> studentsRegistered = new ArrayList<String>();

    public Classdto() { }

    public Classdto(ClassDetails classDetails) {
        this.className = classDetails.getClassName();
        this.classSize = classDetails.getClassSize();
        this.open = classDetails.isOpen();
        this.registrationTime = classDetails.getRegistrationTime();
        this.registrationClosedTime = classDetails.getRegistrationClosedTime();
        this.studentsRegistered = classDetails.getStudentsRegistered();
        this.meetingPeriod = classDetails.getMeetingPeriod() ;
    }


}
