package com.example.dicle_attendance.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dicle_attendance.R;
import com.example.dicle_attendance.activity_persons.ActivityStudent;

import java.io.File;

public class PreLogin extends AppCompatActivity {

    Button teacher;
    Button student;
    String personType;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);



        File file1 = getBaseContext().getFileStreamPath("account.json");
        if(getBaseContext().getFileStreamPath("account.json").exists()) {
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
        }
        setContentView(R.layout.activity_pre_login);


        teacher = findViewById(R.id.teacher);
        student = findViewById(R.id.student);

        teacher.setOnClickListener(view -> {
            personType = "teacher";
            Intent loginPage = new Intent(this, Login.class);
            loginPage.putExtra("personType",personType);
            startActivity(loginPage);
            //finish();
        });
        student.setOnClickListener(view -> {
            personType = "student";
            Intent loginPage = new Intent(this, Login.class);
            loginPage.putExtra("personType",personType);
            startActivity(loginPage);
            //finish();
        });



    }



}
