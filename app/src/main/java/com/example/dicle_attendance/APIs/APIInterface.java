package com.example.dicle_attendance.APIs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    //@POST("login")
    //Call<LoginResponse> loginUser(@Body LoginResponse login);
    @GET("/login")
    Call<LoginResponse> loginUser();

    @GET("/todos/1")
    Call<LoginResponse> test();
}
