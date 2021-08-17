package com.dto;

import com.documents.MeetingPeriods;

import java.time.LocalDateTime;

public class ClassDetails {

    private String className;
    private int classSize;
    private boolean open = false;
    private LocalDateTime registrationTime;
    private LocalDateTime registrationClosedTime;
    private MeetingPeriods meetingPeriod = MeetingPeriods.MWF;


    public ClassDetails(boolean open , LocalDateTime registrationTime )
    {
        this.open = open;
        this.registrationTime = registrationTime;
    }

    public ClassDetails(int classSize, boolean open, LocalDateTime registrationTime, MeetingPeriods meetingPeriod) {
        new ClassDetails(open , registrationTime);
        this.classSize = classSize;
        this.meetingPeriod = meetingPeriod;
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

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public LocalDateTime getRegistrationClosedTime() {
        return registrationClosedTime;
    }

    public void setRegistrationClosedTime(LocalDateTime registrationClosedTime) {
        this.registrationClosedTime = registrationClosedTime;
    }

    public MeetingPeriods getMeetingPeriod() {
        return meetingPeriod;
    }

    public void setMeetingPeriod(MeetingPeriods meetingPeriod) {
        this.meetingPeriod = meetingPeriod;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
