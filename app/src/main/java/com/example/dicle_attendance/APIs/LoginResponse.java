package com.example.dicle_attendance.APIs;

import androidx.activity.result.contract.ActivityResultContracts;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse {


    @SerializedName("student_id")
    private String student_id;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("mobile_activated")
    private  String mobile_activated;

    @SerializedName("is_success")
    private String is_success;


    public String getMobile_activated() {
        return mobile_activated;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getIs_success(){ return is_success; }


    @SerializedName("userId")
    private String userId;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("completed")
    private String completed;

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompleted() {
        return completed;
    }

    public JSONObject getUser(){
        JSONObject user = new JSONObject();


        try {
            user.put("last_name",this.getLast_name());
            user.put("first_name",this.getFirst_name());
            user.put("mobile_activated",this.getMobile_activated());
            user.put("student_id",this.getStudent_id());
            user.put("is_success",this.getIs_success());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return user;
    }
}
