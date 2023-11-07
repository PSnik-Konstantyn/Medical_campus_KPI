package org.example;

import com.google.gson.Gson;

public class JedisActions {
    public static String convertStudentToJson(Student student) {
        Gson gson = new Gson();
        return gson.toJson(student);
    }

    public static Student convertJsonToStudent(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Student.class);
    }
}
