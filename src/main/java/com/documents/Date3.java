package com.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.util.exceptions.DateFormatExeption;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Date3 {
    String day = "00";
    String month = "00";
    String year = "0000";
    String dateForm = "00/00/0000";

     public Date3(){}

    public Date3(String day, String month, String year) {
        if(day.length() != 2 || month.length() != 2 || year.length() != 4) { throw new DateFormatExeption("Improper Date Form try : dd/MM/yyyy"); }
        if(Integer.parseInt(day) > 31 || Integer.parseInt(month) > 12 || Integer.parseInt(year) > 2023) { throw new DateFormatExeption("Improper Date Form Input is to Large"); }
        if(Integer.parseInt(day) < 0 || Integer.parseInt(month) < 0 || Integer.parseInt(year) < 2020) { throw new DateFormatExeption("Improper Date Cannot be negative Or below 2020"); }
        this.day = day;
        this.month = month;
        this.year = year;
        this.dateForm = day + "/" + month + "/" + year ;
    }

    public String getDateForm() {
        return dateForm;
    }

    public void setDateForm(String dd , String mm , String yyyy) {

        if(dd.length() != 2 || mm.length() != 2 || yyyy.length() != 4) { throw new DateFormatExeption("Improper Date Form try : dd/MM/yyyy"); }
        if(Integer.parseInt(dd) > 31 || Integer.parseInt(mm) > 12 || Integer.parseInt(yyyy) > 2023) { throw new DateFormatExeption("Improper Date Form Input is to Large"); }
        if(Integer.parseInt(dd) < 0 || Integer.parseInt(mm) < 0 || Integer.parseInt(yyyy) < 2020) { throw new DateFormatExeption("Improper Date Cannot be negative Or below 2020"); }
        this.dateForm = dd + "/" + mm + "/" + yyyy ;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
