package com.example.thuc.restaurantsdeliveryfood.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Annotation;

public class Data implements SerializedName  {


    @SerializedName("link")
    private String link;

    public String getlink() {
            return link;
        }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String[] alternate() {
        return new String[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
