package org.example;

import com.google.gson.Gson;

import java.io.Serializable;

public class Request implements Serializable {

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isReplied() {
        return isReplied;
    }

    public void setReplied(boolean replied) {
        isReplied = replied;
    }

    public Request( String studentID, String text, boolean isReplied) {
        this.studentID = studentID;
        this.text = text;
        this.isReplied = isReplied;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public static Request fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Request.class);
    }

    private String studentID;
    private String text;
    private boolean isReplied;
}
