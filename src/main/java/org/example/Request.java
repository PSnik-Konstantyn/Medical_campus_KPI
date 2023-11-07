package org.example;

import java.io.Serializable;

public class Request implements Serializable {
    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

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

    public Request(int requestID, String studentID, String text, boolean isReplied) {
        this.requestID = requestID;
        this.studentID = studentID;
        this.text = text;
        this.isReplied = isReplied;
    }

    private int requestID;
    private String studentID;
    private String text;
    private boolean isReplied;
}
