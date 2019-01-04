package com.example.thuc.restaurantsdeliveryfood.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImgur{
    private static String BASE_URL = "https://api.imgur.com/3/";
    private static Gson gson = new GsonBuilder().create();
    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }
}
