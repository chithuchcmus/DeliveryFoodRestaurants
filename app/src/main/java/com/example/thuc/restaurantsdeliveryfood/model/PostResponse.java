package com.example.thuc.restaurantsdeliveryfood.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostResponse implements Serializable {
    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }
}
