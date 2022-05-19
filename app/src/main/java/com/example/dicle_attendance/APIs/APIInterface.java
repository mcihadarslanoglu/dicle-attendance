package com.example.dicle_attendance.APIs;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    //@POST("login")
    //Call<LoginResponse> loginUser(@Body LoginResponse login);
    @GET("/login")
    Call<LoginResponse> loginUser(@Query(value = "loginCredentials",encoded = true) JSONObject loginCredentials);

    @GET("/lessons/getLessons")
    Call<ArrayList<getLessonsResponse>> getLessons(@Query(value = "userCredentials",encoded = true) JSONObject userCredentials);

    @GET("/attendance/submit")
    Call<JSONObject> submitAttendance(@Query(value = "attendanceInformations",encoded = true) JSONObject attendanceInformations);
}
