package com.example.shlez.synagogue;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shlez on 11/23/17.
 */

public class Event implements Serializable {

    String description_event;
    String contact;
    String date;
    String time;


    //    Parameterless Constructor
    public Event( String description_event, String contact, String date,String time) {
        this.description_event=description_event;
        this.contact=contact;
        this.date=date;
        this.time=time;
    }


    //    description_event - Getter & Setter
    public String getDescription_event() {
        return description_event;
    }

    public void setDescription_event(String description_event) {
        this.description_event = description_event;
    }


    //    contact - Getter & Setter
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    //    date- Getter & Setter
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    //    time - Getter & Setter
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}