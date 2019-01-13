package com.example.thuc.restaurantsdeliveryfood.Activity;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.thuc.restaurantsdeliveryfood.R;
import com.example.thuc.restaurantsdeliveryfood.model.PostResponse;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;
    private TextInputEditText oldPassET;
    private TextInputEditText newPassET;
    private TextInputEditText newPassRepeatET;
    private MaterialButton changePassSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassET = findViewById(R.id.old_pass_change);
        newPassET = findViewById(R.id.new_pass_change);
        newPassRepeatET = findViewById(R.id.new_password_repeat);
        changePassSubmit = findViewById(R.id.change_pass_submit);
        dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);

        changePassSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
    }
    private void changePass() {
         String oldPass = oldPassET.getText().toString();
         String newPass = newPassET.getText().toString();
         String confirmPass = newPassRepeatET.getText().toString();

        if (!oldPass.equals("") && !newPass.equals("") && oldPass.equals(newPass))
        {
            dialog.setTitle("Thông Báo");
            dialog.setMessage("Vui lòng chọn mật khẩu khác với mật khẩu ban đầu");
            dialog.show();
        }
        else if (!newPass.equals("") && !confirmPass.equals("")
                && !newPass.equals(confirmPass))
        {
            dialog.setTitle("Thông Báo");
            dialog.setMessage("Mật khẩu bạn vừa nhập không trùng khớp");
            dialog.show();
        }
        else {
            Retrofit retrofit = RetrofitObject.getInstance();
            retrofit.create(CvlApi.class).ChangePass(MainActivity.restaurant.getId(),oldPass,
                    newPass).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    Intent rslt_int = new Intent();
                    if (response.body().getStatus() == 0) {
                        ChangePasswordActivity.this.setResult(MainActivity.CHANGE_PASS_SUCCES, rslt_int);
                    } else {
                        ChangePasswordActivity.this.setResult(MainActivity.CHANGE_PASS_FAIL, rslt_int);
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    Intent rslt_int = new Intent();
                    ChangePasswordActivity.this.setResult(MainActivity.CHANGE_PASS_FAIL, rslt_int);
                    finish();
                }
            });
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
