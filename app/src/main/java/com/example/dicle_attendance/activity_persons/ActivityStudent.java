package com.example.dicle_attendance.activity_persons;

import com.example.dicle_attendance.R;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityStudent extends AppCompatActivity {

    public Button attendanceAction = findViewById(R.id.attendanceAction);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        attendanceAction.setOnClickListener(view -> {

        });
    }


}
