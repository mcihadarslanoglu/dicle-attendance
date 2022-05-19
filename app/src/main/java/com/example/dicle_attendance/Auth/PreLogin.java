package com.example.dicle_attendance.Auth;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dicle_attendance.MainActivity;
import com.example.dicle_attendance.R;


import java.io.File;

public class PreLogin extends AppCompatActivity {

    Button teacher;
    Button student;
    String personType;
    public final int REQUEST_PERMISSION_STATE =  1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        requestPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        requestPermission(Manifest.permission.BLUETOOTH);
        requestPermission(Manifest.permission.BLUETOOTH_ADMIN);
        requestPermission(Manifest.permission.BLUETOOTH_SCAN);
        requestPermission(Manifest.permission.INTERNET);
        requestPermission(Manifest.permission.BLUETOOTH_CONNECT);
        requestPermission(Manifest.permission.BLUETOOTH_ADVERTISE);




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

    public void requestPermission(String permission){

        int permissionCheck = ContextCompat.checkSelfPermission(
                this, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
                showExplanation("İzin gerekli", "Uygulamayı kullanabilmek için tüm izinleri sağladığınızdan emin olunuz.", permission, REQUEST_PERMISSION_STATE);
            } else {
                requestPermission(permission, REQUEST_PERMISSION_STATE);
            }
        } else {
            Toast.makeText(PreLogin.this, "Permission"+permission+ "(already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PreLogin.this, "Permission Granted! " + permissions, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreLogin.this, "Permission Denied! " + permissions, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }



}
