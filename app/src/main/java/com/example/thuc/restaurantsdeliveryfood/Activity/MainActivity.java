package com.example.thuc.restaurantsdeliveryfood.Activity;



import com.example.thuc.restaurantsdeliveryfood.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;


import com.example.thuc.restaurantsdeliveryfood.model.Restaurant;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity  {

    private AlertDialog.Builder dialog ;
    static public Restaurant restaurant = null;
    final static public int EDIT_FOOD_SUCCES=20;
    final static public int EDIT_FOOD_FAIL=21;
    final static public int EDIT_FOOD_STATUS=2;
    final static public int DELETE_FOOD_FAIL=22;
    final static public int DELETE_FOOD_SUCCES=23;

    final static public int ADD_FOOD_STATUS=1;
    final static public int ADD_FOOD_SUCCES=10;
    final static public int ADD_FOOD_FAIL=11;

    final static public int ACCCEPT_BILL_STATUS=3;
    final static public int ACCCEPT_BILL_SUCCES=30;
    final static public int ACCCEPT_BILL_FAIL=31;
    final static public int DENY_BILL_SUCCES=32;
    final static public int DENY_BILL_FAIL=33;

    final static public int CHANGE_PASS_STATUS=4;
    final static public int CHANGE_PASS_SUCCES=40;
    final static public int CHANGE_PASS_FAIL=41;

    private FrameLayout frame;
    private FoodViewFrament foodViewFrament = new FoodViewFrament();
    private BillsViewFragment billViewFragment = new BillsViewFragment();
    private SetttingViewFrament setttingViewFrament = new SetttingViewFrament();
    private RestaurantDetailCommentFragment detailCommentFragment = new RestaurantDetailCommentFragment();
    // More fragment

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_Listfood:
                    loadFragment(foodViewFrament);
                    getSupportActionBar().setTitle("Món Ăn");
                    foodViewFrament.getFood();
                    return true;
                case R.id.navigation_list_bill:
                    loadFragment(billViewFragment);
                    getSupportActionBar().setTitle("Đơn");
                    billViewFragment.getBills();
                    return true;
                case R.id.navigation_setting:
                    loadFragment(setttingViewFrament);
                    getSupportActionBar().setTitle("Cài Đặt");
                    return true;
                case R.id.navigation_list_comment:
                    loadFragment(detailCommentFragment);
                    getSupportActionBar().setTitle("Bình Luận");
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new AlertDialog.Builder(MainActivity.this);
        if(restaurant == null)
            restaurant = (Restaurant) getIntent().getExtras().get("res");

        frame = findViewById(R.id.fragment_container);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(foodViewFrament);
        getSupportActionBar().setTitle("Món Ăn");

    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        dialog.setCancelable(true);
        if(requestCode == MainActivity.ACCCEPT_BILL_STATUS){
            if(resultCode == MainActivity.ACCCEPT_BILL_SUCCES)
            {
                dialog.setMessage("Xác nhận thành công\n");
                billViewFragment.getBills();
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else if(resultCode == MainActivity.ACCCEPT_BILL_FAIL)
            {
                dialog.setMessage("Xác nhận thất bại, vui lòng thử lại sau\n");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else if(resultCode == MainActivity.DENY_BILL_SUCCES){
                dialog.setMessage("Đã từ chối");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }

        }
        else if(requestCode == MainActivity.ADD_FOOD_STATUS)
        {

            if(resultCode == MainActivity.ADD_FOOD_SUCCES)
            {
                dialog.setMessage("Thêm Món Ăn thành công\n");
                foodViewFrament.getFood();
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
            else if(resultCode == MainActivity.ADD_FOOD_FAIL)
            {
                dialog.setMessage("Thêm Món Ăn thất bại, vui lòng thử lại sau \n");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        }
        else if(requestCode == MainActivity.EDIT_FOOD_STATUS)
        {
            if(resultCode == MainActivity.EDIT_FOOD_SUCCES)
            {
                dialog.setMessage("Sửa thông tin món ăn thành công\n");
                foodViewFrament.getFood();
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else if(resultCode == MainActivity.EDIT_FOOD_FAIL)
            {
                dialog.setMessage("Sửa thông tin món ăn thất bại, vui lòng thử lại sau\n");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else if(resultCode == MainActivity.DELETE_FOOD_SUCCES)
            {
                foodViewFrament.getFood();
                dialog.setMessage("Xóa món ăn thành công\n");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else if(resultCode == MainActivity.DELETE_FOOD_FAIL)
            {
                dialog.setMessage("Xóa món ăn thất bại, vui lòng thử lại sau\n");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        }
        else if(requestCode == MainActivity.CHANGE_PASS_STATUS)
        {
            if(resultCode == MainActivity.CHANGE_PASS_SUCCES)
            {
                dialog.setMessage("Thay đổi mật khẩu thành công\n");
                foodViewFrament.getFood();
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
            else if(resultCode == MainActivity.CHANGE_PASS_FAIL)
            {
                dialog.setMessage("Thay đổi mật khẩu thất bại, vui lòng thử lại sau\n");
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        }
        else {
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
