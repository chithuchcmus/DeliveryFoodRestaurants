package com.example.thuc.restaurantsdeliveryfood.Activity;

import com.example.thuc.restaurantsdeliveryfood.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.thuc.restaurantsdeliveryfood.Adapter.CommentViewAdapter;
import com.example.thuc.restaurantsdeliveryfood.model.Comment;
import com.example.thuc.restaurantsdeliveryfood.model.Restaurant;
import com.example.thuc.restaurantsdeliveryfood.model.User;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestaurantDetailCommentFragment extends Fragment {
    private RecyclerView recyclerView;
    private  CommentViewAdapter adapter;
    private List<Comment> comments;
    private  View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get the reference of all the views in the fragment (after being created)
        view = inflater.inflate(R.layout.comment_view_frament, container, false);

        recyclerView = view.findViewById(R.id.comment_list_recycler_view);
        comments = new ArrayList<>();
        adapter = new CommentViewAdapter(comments);
        recyclerView.setAdapter(adapter);

        getComments();

        return view;
    }

    public void getComments() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getComments(MainActivity.restaurant.getId()).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.body() != null){
                    comments.clear();
                    comments.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else
                    Log.e("COMMENT:", "Response body is null");
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.e("COMMENT:", t.getMessage());
            }
        });
    }


    // Interface needs to be implemented by the activity holding this fragment
    public interface hasARestaurantAndUser{
        Restaurant getRestaurant();
        User getUser();
    }
}
