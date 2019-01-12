package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.Adapter.FoodDetailsAdapter;
import com.example.thuc.restaurantsdeliveryfood.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.thuc.restaurantsdeliveryfood.model.Food;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FoodViewFrament extends Fragment implements FoodDetailsAdapter.OnFoodClickListener
{
    RecyclerView recyclerView;
    List<Food> foodList;
    FoodDetailsAdapter adapter;
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.food_view_frament, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.recycle_view_list_restaurant_food);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_button_add_food_in_framentfoodview);
        recyclerView.setHasFixedSize(true);
        foodList = new ArrayList<>();
        adapter = new FoodDetailsAdapter(foodList,this);
        recyclerView.setAdapter(adapter);
        getFood();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoodViewFrament.this.getActivity(),AddFoodActivity.class);
                i.putExtra("data",2);
                FoodViewFrament.this.getActivity().startActivityForResult(i,MainActivity.ADD_FOOD_STATUS);
            }
        });
        return view;
    }

     public  void getFood()
    {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getFoodsList(MainActivity.restaurant.getId()).enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                List<Food> foods = response.body();
                if(foods.size() > 0){
                    FoodViewFrament.this.foodList.clear();
                    FoodViewFrament.this.foodList.addAll(foods);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(FoodViewFrament.this.getActivity(), "Failed1", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(FoodViewFrament.this.getActivity(), "Failed3!", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(FoodViewFrament.this.getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(FoodViewFrament.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onFoodClick(Food f) {
        Intent i = new Intent(this.getActivity(),EditFoodActivity.class);
        i.putExtra("food", (Serializable) f);
        FoodViewFrament.this.getActivity().startActivityForResult(i,MainActivity.EDIT_FOOD_STATUS);
    }

}
