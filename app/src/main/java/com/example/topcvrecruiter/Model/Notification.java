package com.example.topcvrecruiter.Model;

import java.util.Calendar;

public class Notification {
    private int ID;
    private String Content;
    private Calendar Time;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Calendar getTime() {
        return Time;
    }

    public void setTime(Calendar time) {
        Time = time;
    }

    public Notification(int ID, String content, Calendar time) {
        this.ID = ID;
        Content = content;
        Time = time;
    }
}
