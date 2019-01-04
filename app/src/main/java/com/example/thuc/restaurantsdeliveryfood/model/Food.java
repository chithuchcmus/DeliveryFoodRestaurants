package com.example.thuc.restaurantsdeliveryfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food   implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("id_quan_an")
    @Expose
    private int id_quan;
    @SerializedName("ten")
    @Expose
    private String name;
    @SerializedName("gia")
    @Expose
    private int price;
    @SerializedName("mota")
    @Expose
    private String description;
    @SerializedName("anh")
    @Expose
    private String img_path;

    public int getId() {
        return id;
    }

    public void setName(String name)
    {
        this.name =name;
    }
    public void setPrice(int price)
    {
        this.price=price;
    }
    public void setId_quan(int id_quan)
    {
        this.id_quan=id_quan;
    }
    public int getId_quan() {
        return id_quan;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_path() {
        return img_path;
    }
}
