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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassSize() {
        return classSize;
    }

    public void setClassSize(int classSize) {
        this.classSize = classSize;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrationClosedTime() {
        return registrationClosedTime;
    }

    public void setRegistrationClosedTime(String registrationClosedTime) {
        this.registrationClosedTime = registrationClosedTime;
    }

    public String getMeetingPeriod() {
        return meetingPeriod;
    }

    public void setMeetingPeriod(String meetingPeriod) {
        this.meetingPeriod = meetingPeriod;
    }

    public List<String> getStudentsRegistered() {
        return studentsRegistered;
    }

    public void setStudentsRegistered(List<String> studentsRegistered) {
        this.studentsRegistered = studentsRegistered;
    }

    @Override
    public String toString() {
        return "Classdto{" +
                "className='" + className + '\'' +
                ", classSize=" + classSize +
                ", open=" + open +
                ", registrationTime='" + registrationTime + '\'' +
                ", registrationClosedTime='" + registrationClosedTime + '\'' +
                ", meetingPeriod='" + meetingPeriod + '\'' +
                ", studentsRegistered=" + studentsRegistered +
                '}';
    }
}
