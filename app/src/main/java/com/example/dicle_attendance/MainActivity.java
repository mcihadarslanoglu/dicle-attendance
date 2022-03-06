package com.example.dicle_attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Student student = new Student(this);
        //student.start();

        Teacher teacher = new Teacher(this);
        teacher.start();

        new Thread(()->{

        });
    }
}