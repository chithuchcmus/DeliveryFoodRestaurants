package com.example.thuc.restaurantsdeliveryfood.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import okhttp3.RequestBody;

public class ImageUpload implements Serializable {
    @SerializedName("image")
    private RequestBody image;

    public ImageUpload(RequestBody image) {
        this.image = image;
    }

    public RequestBody getImage() {
        return image;
    }
}
