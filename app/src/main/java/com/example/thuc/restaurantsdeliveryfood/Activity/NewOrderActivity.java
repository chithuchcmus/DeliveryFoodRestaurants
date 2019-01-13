package com.example.thuc.restaurantsdeliveryfood.Activity;


import com.example.thuc.restaurantsdeliveryfood.Adapter.NewOrderDetailsAdapter;
import com.example.thuc.restaurantsdeliveryfood.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.thuc.restaurantsdeliveryfood.model.Bill;
import com.example.thuc.restaurantsdeliveryfood.model.BillDetail;
import com.example.thuc.restaurantsdeliveryfood.model.PostResponse;
import com.example.thuc.restaurantsdeliveryfood.retrofit.CvlApi;
import com.example.thuc.restaurantsdeliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NewOrderActivity extends AppCompatActivity
{
    private TextView textViewNew_order_detail_dia_chi_khach;
    private TextView textViewNew_order_detail_dia_chi_quan;
    private TextView textViewNew_order_detial_sumprice;
    private Button buttonAcceptOrder;
    private Button buttonTuChoi;

    private   RecyclerView recyclerView;
    private  List<BillDetail> billDetails;
    private   NewOrderDetailsAdapter adapter;
    private   Bill bill;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail_view);
        bill = (Bill) getIntent().getExtras().get("neworder");
        getView();
        recyclerView = (RecyclerView)findViewById(R.id.bill_detail_recycler_view);
        recyclerView.setHasFixedSize(true);
        billDetails = new ArrayList<>();
        adapter = new NewOrderDetailsAdapter(billDetails);
        recyclerView.setAdapter(adapter);
        getBillDetail();
        setValue();
        buttonAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptBill();
            }
        });
        buttonTuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuChoiBill();
            }
        });
    }
    private void getView()
    {
        textViewNew_order_detial_sumprice = (TextView)findViewById(R.id.bill_detail_total);
        textViewNew_order_detail_dia_chi_khach = (TextView)findViewById(R.id.bill_detail_customer_addr);
        textViewNew_order_detail_dia_chi_quan = (TextView)findViewById(R.id.bill_detail_restaurant_addr);
        buttonAcceptOrder = (Button) findViewById(R.id.buttonAcceptOrder);
        buttonTuChoi = (Button)findViewById(R.id.buttonTuChoiOrder);
    }
    private void setValue()
    {
        textViewNew_order_detail_dia_chi_khach.setText(bill.getDiaChiaGiao());
        textViewNew_order_detail_dia_chi_quan.setText(MainActivity.restaurant.getAddress());
    }
    private void getTotalPrice(List<BillDetail> billDetailList)
    {
        int sum=0;
        for(int i=0;i<billDetailList.size();i++)
        {
            sum+=(billDetailList.get(i).getGia() * billDetailList.get(i).getSoluong());
        }
        textViewNew_order_detial_sumprice.setText(String.valueOf(sum));

    }
    private void TuChoiBill()
    {
        Retrofit retrofit = RetrofitObject.getInstance();
        if(bill.getTrangThai()==0)
        {
            retrofit.create(CvlApi.class).DenyBill(bill.getIdHoaDon(),1).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response)
                {
                    Intent rslt_int = new Intent();
                    if(response.body().getStatus() == 0 )
                    {
                        NewOrderActivity.this.setResult(MainActivity.DENY_BILL_SUCCES, rslt_int);
                    }
                    else
                    {
                        NewOrderActivity.this.setResult(MainActivity.DENY_BILL_FAIL, rslt_int);
                    }
                    finish();
                }


                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {

                    Toast.makeText(NewOrderActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(NewOrderActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    } else {
                        //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                        Toast.makeText(NewOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(NewOrderActivity.this, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();
        }

    }
    private void AcceptBill()
    {
        Retrofit retrofit = RetrofitObject.getInstance();
        if(bill.getTrangThai()==0)
        {
            retrofit.create(CvlApi.class).ResAcceptBill(bill.getIdHoaDon()).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response)
                {
                    Intent rslt_int = new Intent();
                    if(response.body().getStatus() == 0 )
                    {
                        NewOrderActivity.this.setResult(MainActivity.ACCCEPT_BILL_SUCCES, rslt_int);
                    }
                    else
                    {
                        NewOrderActivity.this.setResult(MainActivity.ACCCEPT_BILL_FAIL, rslt_int);
                    }
                    finish();
                }


                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {

                    Toast.makeText(NewOrderActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(NewOrderActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    } else {
                        //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                        Toast.makeText(NewOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(NewOrderActivity.this, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();
        }

    }


    private void getBillDetail() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getDetailBillList(bill.getIdHoaDon()).enqueue(new Callback<List<BillDetail>>() {
            @Override
            public void onResponse(Call<List<BillDetail>> call, Response<List<BillDetail>> response) {
                List<BillDetail> billDetailList = response.body();
                if (billDetailList.size() > 0) {
                    NewOrderActivity.this.getTotalPrice(billDetailList);
                    NewOrderActivity.this.billDetails.clear();
                    NewOrderActivity.this.billDetails.addAll(billDetailList);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(NewOrderActivity.this, "Failed2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<BillDetail>> call, Throwable t) {
                Toast.makeText(NewOrderActivity.this, "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(NewOrderActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                } else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NewOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
