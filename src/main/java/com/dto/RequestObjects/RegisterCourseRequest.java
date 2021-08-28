package com.dto.RequestObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterCourseRequest {

    private String className;
    private int classSize;
    private boolean open = false;
    private String registrationTime;
    private String registrationClosedTime;
    private String intent;
    private String targetCourse;
    private String meetingPeriod = "MWF";
    private List<String> studentsRegistered = new ArrayList<String>();


    public RegisterCourseRequest() {super(); }

    public RegisterCourseRequest(boolean open , String registrationTime )
    {
        this.open = open;
        this.registrationTime = registrationTime;
    }

    public RegisterCourseRequest(int classSize, boolean open, String registrationTime, String meetingPeriod) {
        new RegisterCourseRequest(open , registrationTime);
        this.classSize = classSize;
        this.meetingPeriod = meetingPeriod;
    }
    public RegisterCourseRequest(String className , int classSize, boolean open, String registrationTime, String meetingPeriod) {
        new RegisterCourseRequest(open , registrationTime);
        this.classSize = classSize;
        this.meetingPeriod = meetingPeriod;
        this.className = className;
    }

    public void AddStudentToList(String studentName)
    {
        if(!studentsRegistered.contains(studentName))
        {
            studentsRegistered.add(studentName);
        }
    }

    public void RemoveStudentFromList(String studentsName)
    {
        if(studentsRegistered.contains(studentsName))
        {
            studentsRegistered.remove(studentsName);
        }
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getTargetCourse() {
        return targetCourse;
    }

    public void setTargetCourse(String targetCourse) {
        this.targetCourse = targetCourse;
    }

    public void setStudentsRegistered(List<String> studentsRegistered) {
        this.studentsRegistered = studentsRegistered;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getStudentsRegistered() {
        return studentsRegistered;
    }

    @Override
    public String toString() {
        return "RegisterCourseRequest{" +
                "className='" + className + '\'' +
                ", classSize=" + classSize +
                ", open=" + open +
                ", registrationTime=" + registrationTime +
                ", registrationClosedTime=" + registrationClosedTime +
                ", intent='" + intent + '\'' +
                ", targetCourse='" + targetCourse + '\'' +
                ", meetingPeriod='" + meetingPeriod + '\'' +
                ", studentsRegistered=" + studentsRegistered +
                '}';
    }
}