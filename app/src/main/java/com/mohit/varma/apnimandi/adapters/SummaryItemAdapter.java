package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.UCart;

import java.util.List;

public class SummaryItemAdapter extends RecyclerView.Adapter<SummaryItemAdapter.SummaryItemAdapterViewHolder>{
    private Context context;
    private List<UCart> uCartList;

    public SummaryItemAdapter(Context context, List<UCart> uCartList) {
        this.context = context;
        this.uCartList = uCartList;
    }

    @NonNull
    @Override
    public SummaryItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_summary_item_view,parent,false);
        return new SummaryItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryItemAdapterViewHolder holder, int position) {
        UCart uCart = uCartList.get(position);
        holder.SummarySingleItemName.setText(uCart.getmItemName());
        holder.SummarySingleItemQuantity.setText("Qty: " + uCart.getmItemWeight());
        holder.SummarySingleItemPrice.setText("\u20B9"+uCart.getmItemFinalPrice());
    }

    @Override
    public int getItemCount() {
        return uCartList.size();
    }

    class SummaryItemAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView SummarySingleItemName,SummarySingleItemQuantity,SummarySingleItemPrice;
        public SummaryItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            SummarySingleItemName = itemView.findViewById(R.id.SummarySingleItemName);
            SummarySingleItemQuantity = itemView.findViewById(R.id.SummarySingleItemQuantity);
            SummarySingleItemPrice = itemView.findViewById(R.id.SummarySingleItemPrice);
        }
    }

}
