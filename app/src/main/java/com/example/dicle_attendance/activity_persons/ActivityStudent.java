package com.example.dicle_attendance.activity_persons;

import com.example.dicle_attendance.R;
import com.example.dicle_attendance.persons.Student;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityStudent extends AppCompatActivity {

    public ToggleButton attendanceActionButton;
    public Student person;
    public String attendanceStatus;
    public TextView signState;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        attendanceActionButton = findViewById(R.id.attendanceActionButton);
        signState = findViewById(R.id.signState);
        person = new Student(this);
        attendanceActionButton.setOnClickListener(view -> {
            if(attendanceActionButton.isChecked()){
                if(!person.isSigned()){
                    Log.i("ActivityStudent","Attendance is started");
                    signState.setText("Durum: Bekleniyor...");
                    person.start();
                }
            }
            else if(!attendanceActionButton.isChecked()){
                if (!person.isSigned()){
                    signState.setText("Durum: Yoklam halen alınamadı");
                    person.stop();
                }
               }


        });
        Handler handler = new Handler();
        new Thread(()->{
            final boolean[] flag = {true};
            Log.i("ActivityStudent","in signstate thread");
            while (flag[0]){
                if(person.isSigned()){
                    Log.i("ActivityStudent","student signed "+person.isSigned());
                    flag[0] = false;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            signState.setText("Durum: Yoklamanız alındı");
                            attendanceActionButton.setEnabled(false);
                        }
                    });
                }
                }
        }).start();
    }


}
