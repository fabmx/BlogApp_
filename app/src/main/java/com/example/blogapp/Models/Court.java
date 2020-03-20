package com.example.blogapp.Models;

public class Court {

    private String street, imgUrl;
    private float rating;
    private int drawableResource;

    public Court(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public Court() {
    }

    public Court(String street, String imgUrl, float rating, int drawableResource) {
        this.street = street;
        this.imgUrl = imgUrl;
        this.rating = rating;
        this.drawableResource = drawableResource;
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
