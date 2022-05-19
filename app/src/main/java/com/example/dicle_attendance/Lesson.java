package com.example.dicle_attendance;

public class Lesson {
    String lessonName;
    String lessonCode;

    public Lesson(String lessonName, String lessonCode){
        this.lessonCode = lessonCode;
        this.lessonName = lessonName;
    }

    public String toString(){
        return this.lessonName;
    }
}
