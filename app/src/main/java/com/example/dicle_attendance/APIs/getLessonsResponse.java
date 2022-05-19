package com.example.dicle_attendance.APIs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getLessonsResponse {

    @SerializedName("lesson_name")
    private String lesson_name;

    @SerializedName("lesson_id")
    private String lesson_id;

    public String getLesson_id() {
        return lesson_id;
    }

    public String getLessonName() {
        return lesson_name;
    }
}
