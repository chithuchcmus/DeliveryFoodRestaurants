package com.example.thuc.restaurantsdeliveryfood.Adapter;
import com.example.thuc.restaurantsdeliveryfood.R;
import com.example.thuc.restaurantsdeliveryfood.model.BillDetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewOrderDetailsAdapter extends RecyclerView.Adapter<NewOrderDetailsAdapter.NewOrderFoodInfoViewHolder> {
    List<BillDetail> billDetails ;
    @NonNull
    @Override
    public NewOrderDetailsAdapter.NewOrderFoodInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.order_food_info_item_layout, viewGroup, false);
        return new NewOrderFoodInfoViewHolder(view);

    }
    public  NewOrderDetailsAdapter(List<BillDetail> billDetails)
    {
        this.billDetails = billDetails;
    }
    @Override
    public void onBindViewHolder(@NonNull NewOrderFoodInfoViewHolder newOrderFoodInfoViewHolder, int i) {
        newOrderFoodInfoViewHolder.textViewFoodAmount.setText(String.valueOf(billDetails.get(i).getSoluong()));
        newOrderFoodInfoViewHolder.textViewFoodName.setText(String.valueOf(billDetails.get(i).getTen()));
        newOrderFoodInfoViewHolder.textViewFoodPrice.setText(String.valueOf(billDetails.get(i).getGia()));
    }

    @Override
    public int getItemCount() {
        return billDetails.size();
    }
    public class NewOrderFoodInfoViewHolder extends RecyclerView.ViewHolder {
         TextView textViewFoodName;
         TextView textViewFoodAmount;
         TextView textViewFoodPrice;

        public NewOrderFoodInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodName = itemView.findViewById(R.id.new_order_detail_food_name);
            textViewFoodAmount= itemView.findViewById(R.id.new_order_detail_food_amount);
            textViewFoodPrice = itemView.findViewById(R.id.new_order_detail_food_price);
        }
    }
}
