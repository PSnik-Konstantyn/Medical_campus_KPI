package org.example;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private String studentID;
    private String password;
    private String name;
    private String surname;
    private String group;
    private boolean isIll;
    private Date isHealthy;

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isIll() {
        return isIll;
    }

    public void setIll(boolean ill) {
        isIll = ill;
    }

    public Date getIsHealthy() {
        return isHealthy;
    }

    public void setIsHealthy(Date isHealthy) {
        this.isHealthy = isHealthy;
    }

    public Student(String studentID, String password, String name, String surname, String group, boolean isIll, Date isHealthy) {
        this.studentID = studentID;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.group = group;
        this.isIll = isIll;
        this.isHealthy = isHealthy;
    }
}
