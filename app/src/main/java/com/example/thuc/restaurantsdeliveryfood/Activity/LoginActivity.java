package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.R;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuc.restaurantsdeliveryfood.model.Restaurant;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    String phone_number;
    String password;
    MaterialButton cardViewLogin;
    TextView register;
    AlertDialog.Builder builder;
    boolean isSendingRequest = false;
    final private int SIGN_IN=3;
    final private int SIGN_IN_SUCCES=10;
    final private int SIGN_IN_FAIL=15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        builder = new AlertDialog.Builder(this);
        cardViewLogin = (MaterialButton) findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        cardViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClicked(v);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBtnClicked(v);
            }
        });
    }

    public void registerBtnClicked(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("AAA",1);
        LoginActivity.this.startActivityForResult(intent,SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN)
        {
            builder.setTitle("Thông báo");
            builder.setCancelable(true);

            if(resultCode==SIGN_IN_SUCCES)
            {
                builder.setMessage("Đăng kí thành công, ID mới của bạn là " + data.getExtras().getString("new_id"));
                builder.show();
            }
            if(resultCode == SIGN_IN_FAIL)
            {
                builder.setMessage("Đăng kí thất bại");
                builder.show();
            }
        }
    }

    public void loginBtnClicked(View view) {
        if(isSendingRequest == false)
            isSendingRequest = true;
        else
            Toast.makeText(LoginActivity.this, "Sending request, stop spamming!!", Toast.LENGTH_LONG);

        Retrofit retrofit = RetrofitObject.getInstance();

        GetResnamePassword();
        if(isValidResnamePassword())
            retrofit.create(CvlApi.class)
                .loginRes(phone_number, password)
                .enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                isSendingRequest = false;
                String title, msg;

                if(response.body() != null){
                    List<Restaurant> restaurants = response.body();

                    if(restaurants.size() > 0){
                        msg = restaurants.get(0).toString();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("res",restaurants.get(0));
                        startActivity(i);
                        LoginActivity.this.finish();
                    }
                    else{
                        msg = "Wrong phone number or password!";
                    }
                }
                else
                    msg = "Something is wrong with our server, please try next time!";
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                isSendingRequest = false;
                Toast.makeText(LoginActivity.this, "Request failed! Check your connection and try again.", Toast.LENGTH_SHORT).show();
                Log.e("LOG:", t.getMessage());
                Log.e("LOG:", call.request().url().toString());
            }
        });

        // Hide the keyboard if user click button
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private boolean isValidResnamePassword() {
        return phone_number.length() > 0 && password.length() > 0;
    }

    private void GetResnamePassword() {
        TextInputEditText phoneET = findViewById(R.id.username_login);
        TextInputEditText passET = findViewById(R.id.password_login);
        phone_number = phoneET.getText().toString();
        password = passET.getText().toString();
    }
}
