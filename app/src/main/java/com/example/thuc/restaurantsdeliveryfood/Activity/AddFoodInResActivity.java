package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.R;
import com.example.thuc.restaurantsdeliveryfood.model.Food;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddFoodInResActivity extends AppCompatActivity {

    private String nameFood;
    private String price;
    private String des;
    private Button addFoodInRes;
    private Bitmap srcBitmap;
    private final int RESULT_LOAD_IMAGE = 5;
    EditText nameFoodET;
    EditText priceET;
    EditText desET;
    Food food;
    File srcFileImage;

    private void getInput() {
        nameFood = nameFoodET.getText().toString();
        price = priceET.getText().toString();
        des = desET.getText().toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_in_res_layout);

        nameFoodET = findViewById(R.id.name_food_add_in_res);
        priceET = findViewById(R.id.price_food_add_in_res);
        desET = findViewById(R.id.des_food_add_in_res);

        addFoodInRes = (Button) findViewById(R.id.button_add_food_in_res);

        addFoodInRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodInRes();
            }
        });
    }

    public void onRespone() {
        Log.e("PLEASE", "LOGOUT");
        Intent i = new Intent();
        i.putExtra("AAA", 1);
        setResult(RESULT_OK, i);
        finish();
    }


    private boolean checkInput() {
        getInput();
        if (nameFood.isEmpty() || price.isEmpty() || des.isEmpty())
            return false;
        return true;
    }

    public void addFoodInRes() {
        if(!checkInput())
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Fail!").setMessage(R.string.str_missing_info).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
        else {
                Retrofit retrofit = RetrofitObject.getInstance();

            food = new Food();
            food.setName(nameFood);
            food.setPrice(Integer.valueOf(price));
            food.setId_quan(MainActivity.restaurant.getId());
            retrofit.create(CvlApi.class).newFoodInRes(food).enqueue(new Callback<Food>() {
                @Override
                public void onResponse(Call<Food> call, Response<Food> response) {
                    if(response.body() != null)
                        food = response.body();
                }

                @Override
                public void onFailure(Call<Food> call, Throwable t) {

                }
            });

        }
    }

}
