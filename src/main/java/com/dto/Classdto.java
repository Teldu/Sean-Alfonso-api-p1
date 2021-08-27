package com.dto;

import com.documents.ClassDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.documents.Date3;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Classdto {

    private String className;
    private int classSize;
    private boolean open = false;
    private String registrationTime;
    private String registrationClosedTime;
    private String meetingPeriod;
    private List<String> studentsRegistered = new ArrayList<String>();

    public Classdto() { }

    public Classdto(ClassDetails registerCourseRequest) {
        this.className = registerCourseRequest.getClassName();
        this.classSize = registerCourseRequest.getClassSize();
        this.open = registerCourseRequest.isOpen();
        this.registrationTime = registerCourseRequest.getRegistrationTime();
        this.registrationClosedTime = registerCourseRequest.getRegistrationClosedTime();
        this.studentsRegistered = registerCourseRequest.getStudentsRegistered();
        this.meetingPeriod = registerCourseRequest.getMeetingPeriod() ;
    }


}
