package com.example.dicle_attendance.activity_persons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);
        test = new ArrayList<>();
        test.add("sdasd");
        signsListView = findViewById(R.id.signListView);
        Teacher person = new Teacher(this);
        person.requests.add("test");
        person.requests.add("tasdad");
        signsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        signsAdapter.addAll(person.requests);
        signsListView.setAdapter(signsAdapter);
        signsAdapter.clear();
        person.requests.add("daklpjgdfjg");
        signsAdapter.addAll(person.requests);
        signsAdapter.notifyDataSetChanged();

        //signsAdapter.notifyDataSetChanged();
        /*new Thread(()->{
            while (true){
                Log.i("ActivityTeacher","requests are "+person.requests.toString());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();*/
/*        signsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                AlertDialog.Builder context = new AlertDialog.Builder(getApplicationContext());
                context.setMessage("Silmek istediğinizden emin misiniz ?");
                context.setPositiveButton("Evet", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog , int which ){
                        person.requests.remove(index);
                        signsAdapter.notifyDataSetChanged();
                        //signsListView.setAdapter(null);
                        //signsListView.setAdapter(signsAdapter);

                    }

                });
                context.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                context.show();

            }
        });*/


        attendanceActionButton = findViewById(R.id.attendanceActionButton);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("ActivityTeacher","notifyDatasetChanged "+person.requests.toString());
                person.requests.add("rasdaa");
                signsAdapter.clear();
                signsAdapter.addAll(person.requests);
                signsAdapter.notifyDataSetChanged();
            }
        }, 1000);
        new Thread(()->{
            while (true){
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Log.i("ActivityTeacher","notifyDatasetChanged "+person.requests.toString());
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


        attendanceActionButton.setOnClickListener(view -> {

            if(attendanceActionButton.isChecked()) {
                attendanceActionButton.setChecked(true);
                /*new Thread(() -> {

                    Log.i("ActivityTeacher", "in thread " + attendanceActionButton.isChecked());
                    while (attendanceActionButton.isChecked()) {
                        this.runOnUiThread(()->{
                            Log.i("ActivityTeacher","notifyDatasetChanged "+person.requests.toString());
                            person.requests.add("rasdaa");
                            signsAdapter.clear();
                            signsAdapter.addAll(person.requests);
                            signsAdapter.notifyDataSetChanged();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("ActivityTeacher","notifyDatasetChanged "+person.requests.toString());
                                person.requests.add("rasdaa");

                                signsAdapter.clear();
                                signsAdapter.addAll(person.requests);

                                signsListView.setAdapter(signsAdapter);
                                signsAdapter.notifyDataSetChanged();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }

                }).start();*/

                person.start();
                /*          */


            }
            else if(!attendanceActionButton.isChecked()){
                person.stop();

            }


        });
    }





}
