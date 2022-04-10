package com.example.dicle_attendance.Auth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dicle_attendance.APIs.APIInterface;
import com.example.dicle_attendance.APIs.LoginResponse;
import com.example.dicle_attendance.APIs.RetrofitClient;
import com.example.dicle_attendance.R;
import com.example.dicle_attendance.activity_persons.ActivityStudent;

import com.example.dicle_attendance.activity_persons.ActivityTeacher;
import com.example.dicle_attendance.persons.Student;
import com.example.dicle_attendance.persons.Teacher;

import org.json.JSONException;
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
    String personType;
    Button loginButton;
    TextView personID;
    TextView password;

    JSONObject user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //personType = findViewById(R.id.personType);


        //Log.i("Login","Login button is " + getIntent().getExtras().getString("personType"));






        Log.i("Login","is file exist " + getBaseContext().getFileStreamPath("account.json").exists());
        File file1 = getBaseContext().getFileStreamPath("account.json");
        //Log.i("Login","is file deleted ? " + file1.delete());
        if(getBaseContext().getFileStreamPath("account.json").exists()) {

            File fileAccount = new File(this.getFilesDir(), "account.json");
            byte[] dataByte = new byte[(int) fileAccount.length()];
            FileInputStream streamAccount = null;

            try {
                streamAccount = new FileInputStream(fileAccount);
                streamAccount.read(dataByte);
                streamAccount.close();

                String data = new String(dataByte);
                user = new JSONObject(data);
                Log.i("Login", "Json object: " + user.toString());

                login(user);
                /*
                if (dataJson.get("personType").equals("teacher")) {
                    //Teacher person = new Teacher(this);
                    Log.i("Login", "Teacher object is created");
                    Intent teacherPage = new Intent(this, ActivityTeacher.class);
                    startActivity(teacherPage);
                    finish();


                }
                if (dataJson.get("personType").equals("student")) {
                    //Student person = new Student(this);
                    Log.i("Login", "Student object is created");
                    Intent studentPage = new Intent(this, ActivityStudent.class);
                    startActivity(studentPage);
                    finish();
                }*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        personID = findViewById(R.id.personID);
        password = findViewById(R.id.password);



        loginButton.setOnClickListener(view -> {
            try {
                personType = getIntent().getExtras().getString("personType");
                Log.i("Login","Retrofit start...");
                APIInterface sss = RetrofitClient.getRetrofitInstance().create(APIInterface.class);
                Log.i("Login","Retrofit interface start...");

                Call<LoginResponse> call = sss.loginUser();
                //Call<LoginResponse> call = sss.test();
                Log.i("Login","Retrofit sss...");


                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        JSONObject user = response.body().getUser();

                        Log.i("Login","Request: "+response.body().getUser());
                        try {
                            user.put("personType",personType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        login(user);




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
                //Log.i("Login", "personType is "+personType.isChecked());
                Log.i("Login", "password is "+password.getText());
            }
        },5000);

    }
    private void login(JSONObject user){

        try {
            Log.i("Login","User is_success is "+ user.get("is_success"));
            Context c = getApplicationContext();

            if(user.get("is_success").equals("1")){
                if(user.get("mobile_activated").equals("1")){

                    FileOutputStream fileAccount = openFileOutput("account.json", Context.MODE_PRIVATE);

                    fileAccount.write(user.toString().getBytes(StandardCharsets.UTF_8));
                    fileAccount.close();

                    Log.i("Login", "personType is "+ user.get("personType"));
                    //Log.i("Login", "personType is "+ personTypes.get(personType.isChecked()));
                    if(user.get("personType").equals("teacher")){
                        //Teacher person = new Teacher(this);
                        Log.i("Login","Teacher object is created");
                        Intent teacherPage = new Intent(c, ActivityTeacher.class);
                        teacherPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(teacherPage);

                        //finish();


                    }if(user.get("personType").equals("student")){
                        //Student person = new Student(this);
                        Log.i("Login","Student object is created");
                        Intent studentPage = new Intent(c, ActivityStudent.class);
                        studentPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(studentPage);



                        //finish();
                    }
                }else{
                    Toast.makeText(c,"Account is already activated",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(c,"Invalid credentials",Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



