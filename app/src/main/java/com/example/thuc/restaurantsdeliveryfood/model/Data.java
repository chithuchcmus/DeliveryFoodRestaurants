package com.example.thuc.restaurantsdeliveryfood.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable  {

    @SerializedName("id")
    public String id;
    @SerializedName("link")
    private String link;
    @SerializedName("width")
    public int width;
    @SerializedName("height")
    public int height;

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
