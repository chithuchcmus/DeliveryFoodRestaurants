package com.example.thuc.restaurantsdeliveryfood.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageInfoRespone implements Serializable {
    @SerializedName("data")
    public Data data;
    @SerializedName("succes")
    public boolean succes;
    @SerializedName("status")
    public int status;

    public  String getURL()
    {
        if(data!=null)
        {
            return data.getLink();
        }
        return null;
    }
}
