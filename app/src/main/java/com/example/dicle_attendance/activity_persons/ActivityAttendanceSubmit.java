package com.example.dicle_attendance.activity_persons;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.dicle_attendance.APIs.APIInterface;
import com.example.dicle_attendance.APIs.LoginResponse;
import com.example.dicle_attendance.APIs.RetrofitClient;
import com.example.dicle_attendance.APIs.getLessonsResponse;
import com.example.dicle_attendance.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAttendanceSubmit extends AppCompatActivity {

    public ArrayList<String> attendanceList;
    Intent intent;
    Button submitButton;
    TextView lessonCountField;
    String lessonCount;
    Spinner lessons;
    ArrayList<getLessonsResponse> lessonsArrayList;
    String targetLesson;
    JSONObject user;
    Context context;
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_submit);
        context = this;
        intent = getIntent();
        try {
            user = new JSONObject(intent.getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lessons = findViewById(R.id.lessons);

        attendanceList = intent.getStringArrayListExtra("attendanceList");
        Log.i("ActivityAttendanceSubmit",attendanceList.toString());
        submitButton = findViewById(R.id.attendanceSubmitButton);
        lessonCountField = findViewById(R.id.lessonTime);

        new Thread(()->{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadLessons();
                }
            });
        }).start();



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    targetLesson = lessonsArrayList.get(lessons.getSelectedItemPosition()).getLesson_id();
                    lessonCount = lessonCountField.getText().toString();
                    Log.i("ActivityAttendanceSubmit","Selected item is " + targetLesson);
                    submitAttendance();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



}

@SuppressLint("LongLogTag")
public void submitAttendance(){
        JSONObject attendanceInformations = new JSONObject();
    try {
        attendanceInformations.put("id",user.get("id"));

        Log.i("ActivityAttendanceSubmit","attendanceList is "+attendanceList.toString());
        String _attendanceList = TextUtils.join(",",attendanceList);
        attendanceInformations.put("attendanceList", _attendanceList);


        attendanceInformations.put("lesson_id",targetLesson);
        attendanceInformations.put("lessonCount",lessonCount);
        Log.i("ActivityAttendanceSubmit","attendance list is" + attendanceInformations.get("attendanceList").toString());
    } catch (JSONException e) {
        e.printStackTrace();
    }
    Log.i("ActivityAttendanceSubmit","Retrofit start...");
        APIInterface sss = RetrofitClient.getRetrofitInstance().create(APIInterface.class);
        Log.i("ActivityAttendanceSubmit","Retrofit interface start...");

        Call<JSONObject> call = sss.submitAttendance(attendanceInformations);

        Log.i("ActivityAttendanceSubmit","Retrofit sss...");
        Log.i("ActivityAttendanceSubmit","attendanceInformations " + attendanceInformations);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("ActivityAttendanceSubmit",response.body().toString());


            }
            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("ActivityAttendanceSubmit","Request: "+t.toString());
            }
        });

}
public void loadLessons(){
        JSONObject userCredentials = new JSONObject();

    try {
        userCredentials.put("id",user.get("id"));
        userCredentials.put("personType",user.get("personType"));
    } catch (JSONException e) {
        e.printStackTrace();
    }
    Log.i("ActivityAttendanceSubmit","Retrofit start...");
        APIInterface sss = RetrofitClient.getRetrofitInstance().create(APIInterface.class);
        Log.i("ActivityAttendanceSubmit","Retrofit interface start...");
        Call<ArrayList<getLessonsResponse>> call = sss.getLessons(userCredentials);
        Log.i("ActivityAttendanceSubmit","Retrofit sss...");
        Log.i("ActivityAttendanceSubmit","User is "+userCredentials.toString());
        call.enqueue(new Callback<ArrayList<getLessonsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<getLessonsResponse>> call, Response<ArrayList<getLessonsResponse>> response) {
                Log.i("ActivityAttendanceSubmit",response.body().toString());
                lessonsArrayList = response.body();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                for (getLessonsResponse item:response.body()
                ) {
                    adapter.add(item.getLessonName());
                }
                lessons.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ArrayList<getLessonsResponse>> call, Throwable t) {
                Log.e("Login","Request: "+t.toString());
            }
        });

    }
}