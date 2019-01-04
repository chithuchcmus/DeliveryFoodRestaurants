package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.R;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thuc.restaurantsdeliveryfood.model.Restaurant;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private String nameRes;
    private String phone;
    private String email;
    private String password;
    private String address;
    final private int SIGN_IN_SUCCES=10;
    final private int SIGN_IN_FAIL=15;

    EditText nameResET;
    EditText phoneET;
    EditText emailET;
    EditText passwordET;
    EditText addressET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_restaurant_activity);
        nameResET = findViewById(R.id.nameQuanEdit);
        phoneET = findViewById(R.id.phoneEdit);
        passwordET = findViewById(R.id.passwordEdit);
        emailET = findViewById(R.id.emailEdit);
        addressET  = findViewById(R.id.addresEdit);

    }

    private void getInput(){
        nameRes = nameResET.getText().toString();
        phone = phoneET.getText().toString();
        password = phoneET.getText().toString();
        email = emailET.getText().toString();
        address = addressET.getText().toString();
    }

    private boolean checkInput(){
        getInput();
        if(nameRes.isEmpty()  || phone.isEmpty() || password.isEmpty() || email.isEmpty() || address.isEmpty())
            return false;
        return true;
    }


    public void submitRegisterInfoClicked(View view) {
        if(!checkInput())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fail!").setMessage(R.string.str_missing_info).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
        else{
            Retrofit retrofit = RetrofitObject.getInstance();

            Map<String, String> info = new HashMap<String, String>();
            info.put("sdt", phone);
            info.put("ten", nameRes);
            info.put("pass", password);
            info.put("email", email);
            info.put("dchi",address);
            final Intent i = new Intent();
            retrofit.create(CvlApi.class).newRes(info).enqueue(new Callback<List<Restaurant>>() {
                @Override
                public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                    String msg;
                    if(response.body() != null){
                        if(response.body().size() > 0)
                        {
                            SignUpActivity.this.setResult(SIGN_IN_SUCCES, i);
                            i.putExtra("new_id",String.valueOf(response.body().get(0).getId()));
                        }
                        else
                            SignUpActivity.this.setResult(SIGN_IN_FAIL, i);
                        finish();
                    }
                    else
                    {
                        msg = "";
                        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Failed3!", Toast.LENGTH_SHORT).show();

                    if (t instanceof IOException) {
                        Toast.makeText(SignUpActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
