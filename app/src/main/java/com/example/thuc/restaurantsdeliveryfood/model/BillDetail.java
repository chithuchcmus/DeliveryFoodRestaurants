package com.example.thuc.restaurantsdeliveryfood.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BillDetail implements Serializable {
    @SerializedName("ten")
    private String ten;
    @SerializedName("id_chi_tiet")
    private int id_chi_tiet;
    @SerializedName("id_mon_an")
    private int id_mon_an;

    @SerializedName("soluong")
    private int soluong;
    @SerializedName("gia")
    private int gia;

    public int getId_chi_tiet() {
        return id_chi_tiet;
    }

    public int getId_mon_an() {
        return id_mon_an;
    }

    public String getTen() {
        return ten;
    }

    public int getSoluong() {
        return soluong;
    }
    public int getGia() {
        return gia;
    }
}
