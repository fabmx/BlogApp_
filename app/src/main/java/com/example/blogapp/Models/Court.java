package com.example.blogapp.Models;

import java.io.Serializable;

public class Court implements Serializable {

    private String street, imgUrl;
    private float rating;
    private int drawableResource;
    private double lat, lng;

    public Court(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public Court() {
    }

    public Court(String street, String imgUrl, float rating, int drawableResource, double lat, double lng) {
        this.street = street;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.drawableResource = drawableResource;
        this.lat = lat;
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }
}
