package com.example.thuc.restaurantsdeliveryfood.Activity;
import com.example.thuc.restaurantsdeliveryfood.Adapter.BillViewAdapter;
import com.example.thuc.restaurantsdeliveryfood.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.thuc.restaurantsdeliveryfood.model.Bill;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class BillsViewFragment extends Fragment implements BillViewAdapter.BillViewOnClickListener {
    int resid;
    RecyclerView recyclerView;
    List<Bill> bills;
    BillViewAdapter adapter;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resid = MainActivity.restaurant.getId();
        View view = inflater.inflate(R.layout.fragment_bills_view, container, false);
        recyclerView = view.findViewById(R.id.bill_view_recycler_view);
        bills = new ArrayList<>();
        adapter = new BillViewAdapter(bills, this);
        recyclerView.setAdapter(adapter);
        getBills();
        initThreadOrdernNeedDelivery();
        return view;
    }


    private void initThreadOrdernNeedDelivery() {
        final Runnable updateData = new Runnable() {
            @Override
            public void run() {
                getBills();
            }
        };
        Runnable operationFuc = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    recyclerView.postDelayed(updateData, 2000);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        Thread longOperation = new Thread(operationFuc);
        longOperation.start();
    }

    public void getBills() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getBillsListNeedToAccept(resid, 0).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                List<Bill> billsList = response.body();
                if (billsList.size() >= 0) {
                    bills.clear();
                    bills.addAll(billsList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Toast.makeText(BillsViewFragment.this.getActivity(), "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(BillsViewFragment.this.getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                } else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(BillsViewFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClickBillView(Bill r) {
        // Call the bill detail
        Intent i = new Intent(this.getActivity(), NewOrderActivity.class);
        i.putExtra("neworder", r);
        this.getActivity().startActivityForResult(i, MainActivity.ACCCEPT_BILL_STATUS);
    }


}
