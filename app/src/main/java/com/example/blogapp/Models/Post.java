package com.example.blogapp.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String title;
    private String description;
    private String picture;
    private String username;
    private String userId;
    private String userPhoto;
    private String day;
    private Object timeStamp;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Post(String title, String description, String picture, String username, String userId, String userPhoto, String day) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.username = username;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.day = day;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("picture", picture);
        result.put("userId", userId);
        result.put("userPhoto", userPhoto);
        result.put("timestamp", timeStamp.toString());

        return result;
    }


    public String getDay() {
        return day;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}