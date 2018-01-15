package com.example.shlez.synagogue;

import java.util.Date;

/**
 * Created by Shlez on 1/13/18.
 */

public class UpdatePost {

    private String email;
    private String title;
    private String message;


    public UpdatePost() {

        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String date;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
