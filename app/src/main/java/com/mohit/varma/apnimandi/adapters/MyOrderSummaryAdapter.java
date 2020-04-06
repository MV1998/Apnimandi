package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.UCart;

import java.util.List;

public class MyOrderSummaryAdapter extends RecyclerView.Adapter<MyOrderSummaryAdapter.MyOrderSummaryAdapterViewHolder> {
    private Context context;
    private List<UCart> uCartList;

    public MyOrderSummaryAdapter(Context context, List<UCart> uCartList) {
        this.context = context;
        this.uCartList = uCartList;
    }

    @NonNull
    @Override
    public MyOrderSummaryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myorder_summary_single_item_view, parent, false);
        return new MyOrderSummaryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderSummaryAdapterViewHolder holder, int position) {
        UCart uCart = uCartList.get(position);
        holder.SummarySingleItemName.setText(uCart.getmItemName());
        holder.SummarySingleItemQuantity.setText("Qty: " + uCart.getmItemWeight());
        holder.SummarySingleItemPrice.setText("\u20B9" + uCart.getmItemFinalPrice());
    }

    @Override
    public int getItemCount() {
        return uCartList.size();
    }

    class MyOrderSummaryAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView SummarySingleItemName, SummarySingleItemQuantity, SummarySingleItemPrice;

        public MyOrderSummaryAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            SummarySingleItemName = itemView.findViewById(R.id.SummarySingleItemName);
            SummarySingleItemQuantity = itemView.findViewById(R.id.SummarySingleItemQuantity);
            SummarySingleItemPrice = itemView.findViewById(R.id.SummarySingleItemPrice);
        }
    }

}

