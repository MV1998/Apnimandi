package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.OrderStatus;
import com.mohit.varma.apnimandi.model.Orders;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderAdapterViewHolder>{

    private Context context;
    private List<Orders> ordersList;

    public MyOrderAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public MyOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_order_single_item_view,parent,false);
        return new MyOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapterViewHolder holder, int position) {
        Orders orders = ordersList.get(position);
        holder.MyOrderSingleItemOrderNumber.setText("Order No - "+orders.getOrderId());
        holder.MyOrderSingleItemGrandTotal.setText("\u20B9"+orders.getGrandTotal());
        setOrderStatus(orders.getOrderStatus(),holder.MyOrderSingleItemOrderStatus);
        if(ordersList.get(position).getuCartList().size()>0){
            setAdapter(ordersList.get(position).getuCartList(),holder.MyOrderSingleItemRecyclerView);
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class MyOrderAdapterViewHolder extends RecyclerView.ViewHolder{
        private View MyOrderSingleItemRootView;
        private MaterialButton MyOrderSingleItemCancelOrderButton,MyOrderSingleItemTrackOrderButton;
        private TextView MyOrderSingleItemOrderNumber,MyOrderSingleItemGrandTotal,MyOrderSingleItemOrderStatus;
        private RecyclerView MyOrderSingleItemRecyclerView;
        public MyOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            MyOrderSingleItemRootView = itemView.findViewById(R.id.MyOrderSingleItemRootView);
            MyOrderSingleItemCancelOrderButton = itemView.findViewById(R.id.MyOrderSingleItemCancelOrderButton);
            MyOrderSingleItemTrackOrderButton = itemView.findViewById(R.id.MyOrderSingleItemTrackOrderButton);
            MyOrderSingleItemOrderNumber = itemView.findViewById(R.id.MyOrderSingleItemOrderNumber);
            MyOrderSingleItemGrandTotal = itemView.findViewById(R.id.MyOrderSingleItemGrandTotal);
            MyOrderSingleItemOrderStatus = itemView.findViewById(R.id.MyOrderSingleItemOrderStatus);
            MyOrderSingleItemRecyclerView = itemView.findViewById(R.id.MyOrderSingleItemRecyclerView);
        }
    }

    public void setAdapter(List<UCart> uCartList,RecyclerView recyclerView) {
        if (uCartList != null && uCartList.size() > 0) {
            MyOrderSummaryAdapter myOrderSummaryAdapter = new MyOrderSummaryAdapter(context, uCartList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(myOrderSummaryAdapter);
        }
    }

    public void setOrderStatus(OrderStatus orderStatus,TextView MyOrderSingleItemOrderStatus){
        switch (orderStatus){
            case ORDER_PLACED:
                MyOrderSingleItemOrderStatus.setText(Constants.ORDER_PLACED);
                break;
            case SHIPPED:
                MyOrderSingleItemOrderStatus.setText(Constants.SHIPPED);
                break;
            case CANCELLED:
                MyOrderSingleItemOrderStatus.setText(Constants.CANCELLED);
                break;
            case DELIVERED:
                MyOrderSingleItemOrderStatus.setText(Constants.DELIVERED);
                break;
            case PROCESSING:
                MyOrderSingleItemOrderStatus.setText(Constants.PROCESSING);
                break;
        }
    }
}
