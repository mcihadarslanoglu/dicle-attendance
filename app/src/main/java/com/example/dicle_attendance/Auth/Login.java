package com.example.dicle_attendance.Auth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dicle_attendance.APIs.APIInterface;
import com.example.dicle_attendance.APIs.LoginResponse;
import com.example.dicle_attendance.APIs.RetrofitClient;
import com.example.dicle_attendance.R;
import com.example.dicle_attendance.activity_persons.ActivityStudent;

import com.example.dicle_attendance.persons.Student;
import com.example.dicle_attendance.persons.Teacher;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    ToggleButton personType;
    Button loginButton;
    TextView personID;
    TextView password;
    HashMap<String,String > personTypes = new HashMap<String,String>();
    JSONObject dataJson=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        personType = findViewById(R.id.personType);
        loginButton = findViewById(R.id.loginButton);
        Log.i("Login","Login button is " + personType.toString());

        personID = findViewById(R.id.personID);
        password = findViewById(R.id.password);


        personTypes.put("true","teacher");
        personTypes.put("false","student");




        Log.i("Login","is file exist " + getBaseContext().getFileStreamPath("account.json").exists());
        if(getBaseContext().getFileStreamPath("account.json").exists()){

            File fileAccount = new File(this.getFilesDir(), "account.json");
            byte[] dataByte = new byte[(int) fileAccount.length()];
            FileInputStream streamAccount = null;

            try {
                streamAccount = new FileInputStream(fileAccount);
                streamAccount.read(dataByte);
                streamAccount.close();

                String data = new String(dataByte);
                dataJson = new JSONObject(data);
                Log.i("Login","Json object: "+ dataJson.toString());

                if(dataJson.get("personType").equals("teacher")){
                    Teacher person = new Teacher(this);
                    Log.i("Login","Teacher object is created");


                }if(dataJson.get("personType").equals("student")){
                    Student person = new Student(this);
                    Log.i("Login","Student object is created");
                    Intent studentPage = new Intent(this, ActivityStudent.class);
                    startActivity(studentPage);
                    finish();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }





        loginButton.setOnClickListener(view -> {
            try {

                Log.i("Login","Retrofit start...");
                APIInterface sss = RetrofitClient.getRetrofitInstance().create(APIInterface.class);
                Log.i("Login","Retrofit interface start...");

                Call<LoginResponse> call = sss.loginUser();
                //Call<LoginResponse> call = sss.test();
                Log.i("Login","Retrofit sss...");

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.i("Login","Request: "+response.body().getUser());
                        Log.i("Login", "Request Body: "+  response.body().getUser().toString());
                        /*
                        File fileAccount = new File(getFilesDir(), "account.json");
                        byte[] dataByte = new byte[(int) fileAccount.length()];
                        FileInputStream streamAccount = null;


                         */
                        try {
                            /*
                            streamAccount = new FileInputStream(fileAccount);
                            streamAccount.read(dataByte);
                            streamAccount.close();

                            */
                            //String data = new String(dataByte);
                            //dataJson = new JSONObject(data);
                            //Log.i("Login","Json object: "+ dataJson.toString());
                            Context c = getApplicationContext();
                            if(dataJson.get("personType").equals("teacher")){
                                /*
                                * Context kısmı sıkıntılı olabilir, eğer aksiyonlar düzgün çalışmazsa buraya bak.
                                * */
                                Teacher person = new Teacher(c);
                                Log.i("Login","Teacher object is created");
                                //Intent teacherPage = new Intent(this,);
                                //finish();

                            }if(dataJson.get("personType").equals("student")){
                                Student person = new Student(c);
                                Log.i("Login","Student object is created");

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("Login","Request: "+t.toString());
                    }
                });


                //Teacher person = new Teacher(this);
                /*
                FileOutputStream fileAccount1 = this.openFileOutput("account.json", Context.MODE_PRIVATE);
                fileAccount1.write("{\"first_name\":\"Muhammed Cihad\",\"last_name\":\"ARSLANO\\u011eLU\",\"mobile_activated\":1,\"student_id\":18354012,\"personType\":\"student\"}".getBytes(StandardCharsets.UTF_8));
                fileAccount1.close();



                File file1 = getBaseContext().getFileStreamPath("account.json");
                Log.i("Login","is file exist ? " + file1.exists());


                   */
                //Log.i("Login","is file deleted ? " + file1.delete());
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
            Log.i("Login", "personID is "+personID.getText());
            Log.i("Login", "personType is "+personTypes.get(String.valueOf(personType.isChecked())));
            Log.i("Login", "password is "+password.getText());
            */


        });








        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("Login", "personID is "+personID.getText());
                Log.i("Login", "personType is "+personType.isChecked());
                Log.i("Login", "password is "+password.getText());
            }
        },5000);
    }
    }
}



