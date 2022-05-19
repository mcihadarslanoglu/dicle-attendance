package com.example.dicle_attendance.activity_persons;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dicle_attendance.MainActivity;
import com.example.dicle_attendance.R;
import com.example.dicle_attendance.persons.Student;
import com.example.dicle_attendance.persons.Teacher;

import java.util.ArrayList;

public class ActivityTeacher extends AppCompatActivity {

    public ToggleButton attendanceActionButton;
    public Student person;
    public String attendanceStatus;
    public TextView signState;
    public ArrayAdapter<String> signsAdapter;
    public ListView signsListView;
    public ArrayList<String> test;
    public Intent intent;
    public Button attendanceFinishButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);
        intent = getIntent();

        signsListView = findViewById(R.id.signListView);
        attendanceFinishButton = findViewById(R.id.attendanceSubmitButton);
        Teacher person = new Teacher(this,intent.getStringExtra("user"));

        signsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        signsAdapter.addAll(person.requests);
        signsListView.setAdapter(signsAdapter);
        signsAdapter.addAll(person.requests);
        signsAdapter.notifyDataSetChanged();

        attendanceFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent attendanceSubmitPage = new Intent(getApplicationContext(), ActivityAttendanceSubmit.class);
                attendanceSubmitPage.putExtra("user",person.user.toString());
                attendanceSubmitPage.putExtra("attendanceList",person.requests);
                startActivity(attendanceSubmitPage);
            }
        });
        signsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTeacher.this);
                builder.setMessage("Silmek istediğinizden emin misiniz ?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        person.requests.remove(index);
                        signsAdapter.notifyDataSetChanged();
                        //signsListView.setAdapter(null);
                        //signsListView.setAdapter(signsAdapter);

                    }
                });
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                }





        });


        attendanceActionButton = findViewById(R.id.attendanceActionButton);





        attendanceActionButton.setOnClickListener(view -> {

            if(attendanceActionButton.isChecked()) {
                attendanceActionButton.setChecked(true);
                new Thread(()->{
                    while (attendanceActionButton.isChecked()){
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //Log.i("ActivityTeacher","notifyDatasetChanged "+person.requests.toString());
                                        //person.requests.add("rasdaa");
                                        signsAdapter.clear();
                                        signsAdapter.addAll(person.requests);
                                        signsAdapter.notifyDataSetChanged();
                                    }
                                }


                        );
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();


                person.start();
                /*          */


            }
            else if(!attendanceActionButton.isChecked()){
                person.stop();

            }


        });
    }





}
