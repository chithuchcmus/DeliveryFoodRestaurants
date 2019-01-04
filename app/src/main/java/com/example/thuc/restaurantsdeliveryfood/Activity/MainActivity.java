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

    AlertDialog.Builder dialog ;
    static public Restaurant restaurant = null;
    private FrameLayout frame;
    private FoodViewFrament foodViewFrament = new FoodViewFrament();
    private BillsViewFragment billViewFragment = new BillsViewFragment();
    // More fragment

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(foodViewFrament);
                    foodViewFrament.getFood();
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(billViewFragment);
                    billViewFragment.getBills();
                    return true;
                case R.id.navigation_notifications:
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
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK)
            {
                dialog.setTitle("Thông báo \n");
                dialog.setMessage("Xác nhận thành công\n");
                billViewFragment.getBills();

            }
            if(resultCode == Activity.RESULT_CANCELED)
            {
                dialog.setTitle("Xác nhận thất bại\n");
            }
            if(resultCode == 2812){
                dialog.setMessage("Đã từ chối");
            }
        }
        else
        {
            Log.d(TAG, String.format("got result %d for %d ",resultCode, requestCode));
        }
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
