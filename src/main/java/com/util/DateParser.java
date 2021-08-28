package com.util;

import com.util.exceptions.InvalidRequestException;

import java.time.LocalDateTime;

public class DateParser {
    int month;
    int day;
    int year;

    public DateParser() { }
    public DateParser(String inDate)
    {
         String[] outVal = inDate.split("/");

        this.month = Integer.parseInt(outVal[0]);
        this.day = Integer.parseInt(outVal[1]);
        this.year = Integer.parseInt(outVal[2]);
    }

    /**
     * creates time window and returns true if the current date is in the created window
     * window opening must be first
     * @param inDate
     * @param inDate2
     * @return
     */
    public boolean Window(String inDate , String inDate2)
    {
        LocalDateTime currentTime = LocalDateTime.now();
        // current date value to compare with margins
        int monthValue = currentTime.getMonthValue();
        int dayOfMonth = currentTime.getDayOfMonth();
        int currentTimeYear = currentTime.getYear(); // obvioulsy in our case this will be 2021 but fo the sake of scalability we used getter

        // split in String to create margin 1
        String[] rawTime = inDate.split("/");
        // split in String to create margin 2
        String[] rawClosedTime = inDate2.split("/");

        //margin 1
        int inDateMonth = Integer.parseInt(rawTime[0]);
        int inDateDay = Integer.parseInt(rawTime[1]);
        int inDateYear = Integer.parseInt(rawTime[2]);
        // margin 2
        int inDate2Month = Integer.parseInt(rawClosedTime[0]);
        int inDate2Day = Integer.parseInt(rawClosedTime[1]);
        int inDate2Year = Integer.parseInt(rawClosedTime[2]);

        // checking if months are value equivalent , if so checking which day is later
        if((inDateMonth == monthValue && inDateDay <= dayOfMonth) || (inDateMonth < monthValue))
        {
            // // checking if months are value equivalent , if so checking which day is earlier
            if( (monthValue == inDate2Month && inDate2Day >= dayOfMonth) || (monthValue > inDate2Month) )
            {
                return true;
            }else{

                return false;

            }

        }
            return false;

    }
    /**
     * takes in html Dates (yyyy-mm-dd)
     * creates time window and returns true if the current date is in the created window
     * window opening must be first
     * @param inDate
     * @param inDate2
     * @return
     */
    public boolean htmlWindow(String inDate , String inDate2)
    {
        LocalDateTime currentTime = LocalDateTime.now();
        // current date value to compare with margins
        int monthValue = currentTime.getMonthValue();
        int dayOfMonth = currentTime.getDayOfMonth();
        int currentTimeYear = currentTime.getYear(); // obvioulsy in our case this will be 2021 but fo the sake of scalability we used getter

        // split in String to create margin 1
        String[] rawTime = inDate.split("-");
        // split in String to create margin 2
        String[] rawClosedTime = inDate2.split("-");

        //margin 1
        int inDateMonth = Integer.parseInt(rawTime[1]);
        int inDateDay = Integer.parseInt(rawTime[2]);
        int inDateYear = Integer.parseInt(rawTime[0]);
        // margin 2
        int inDate2Month = Integer.parseInt(rawClosedTime[1]);
        int inDate2Day = Integer.parseInt(rawClosedTime[2]);
        int inDate2Year = Integer.parseInt(rawClosedTime[0]);

        // checking if months are value equivalent , if so checking which day is later
        if((inDateMonth == monthValue && inDateDay <= dayOfMonth) || (inDateMonth < monthValue))
        {
            // // checking if months are value equivalent , if so checking which day is earlier
            if( (monthValue == inDate2Month && inDate2Day >= dayOfMonth) || (monthValue > inDate2Month) )
            {
                return true;
            }else{

                return false;

            }

        }
        return false;

    }
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
