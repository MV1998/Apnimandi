package com.mohit.varma.apnimandi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.OrderStatus;
import com.mohit.varma.apnimandi.model.Orders;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderAdapterViewHolder> {
    private static final String TAG = "MyOrderAdapter";
    private Context context;
    private List<Orders> ordersList;
    private Orders orders;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public MyOrderAdapter(Context context, List<Orders> ordersList, DatabaseReference databaseReference) {
        this.context = context;
        this.ordersList = ordersList;
        this.databaseReference = databaseReference;
        this.firebaseAuth = MyApplication.getFirebaseAuth();
    }

    @NonNull
    @Override
    public MyOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_order_single_item_view, parent, false);
        return new MyOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapterViewHolder holder, int position) {
        orders = ordersList.get(position);
        holder.MyOrderSingleItemOrderNumber.setText("Order No - " + orders.getOrderId());
        holder.MyOrderSingleItemGrandTotal.setText("\u20B9" + orders.getGrandTotal());
        setOrderStatus(orders.getOrderStatus(), holder.MyOrderSingleItemOrderStatus);
        if (ordersList.get(position).getEstimateDeliveryDate() != null) {
            if (!ordersList.get(position).getEstimateDeliveryDate().isEmpty()) {
                holder.MyOrderSingleItemOrderStatusEstimateDelivery.setText("Estimate Delivery : " + orders.getEstimateDeliveryDate());
            }
        }
        if (ordersList.get(position).getuCartList().size() > 0) {
            setAdapter(ordersList.get(position).getuCartList(), holder.MyOrderSingleItemRecyclerView);
        }

        if (ordersList != null && ordersList.size() > 0) {
            if (ordersList.get(position).getOrderStatus() == OrderStatus.CANCELLED) {
                holder.MyOrderSingleItemCancelOrderButton.setVisibility(View.GONE);
                holder.MyOrderSingleItemTrackOrderButton.setVisibility(View.GONE);
                holder.MyOrderSingleItemOrderStatusEstimateDelivery.setVisibility(View.GONE);
            } else {
                holder.MyOrderSingleItemCancelOrderButton.setText("Cancel Order");
                holder.MyOrderSingleItemCancelOrderButton.setClickable(true);
                holder.MyOrderSingleItemOrderStatusEstimateDelivery.setVisibility(View.VISIBLE);
            }
        }

        holder.MyOrderSingleItemCancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders = ordersList.get(position);
                orders.setOrderStatus(OrderStatus.CANCELLED);
                Log.d(TAG, "onClick: " + new Gson().toJson(orders));
                databaseReference.child("Orders").orderByChild("orderId").equalTo(orders.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                item.getRef().setValue(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        holder.MyOrderSingleItemCancelOrderButton.setText("Order Cancelled");
                                        holder.MyOrderSingleItemCancelOrderButton.setClickable(false);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if (firebaseAuth != null) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("MyOrders").orderByChild("orderId").equalTo(orders.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                                            item.getRef().setValue(orders);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }
        });

        holder.MyOrderSingleItemOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Grand total has included delivery charge also.");
                builder.setTitle("Delivery Charge");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class MyOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        private View MyOrderSingleItemRootView;
        private MaterialButton MyOrderSingleItemCancelOrderButton, MyOrderSingleItemTrackOrderButton;
        private TextView MyOrderSingleItemOrderNumber, MyOrderSingleItemGrandTotal, MyOrderSingleItemOrderStatus, MyOrderSingleItemOrderStatusEstimateDelivery;
        private RecyclerView MyOrderSingleItemRecyclerView;
        private ImageView MyOrderSingleItemOrderInfo;

        public MyOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            MyOrderSingleItemRootView = itemView.findViewById(R.id.MyOrderSingleItemRootView);
            MyOrderSingleItemCancelOrderButton = itemView.findViewById(R.id.MyOrderSingleItemCancelOrderButton);
            MyOrderSingleItemTrackOrderButton = itemView.findViewById(R.id.MyOrderSingleItemTrackOrderButton);
            MyOrderSingleItemOrderNumber = itemView.findViewById(R.id.MyOrderSingleItemOrderNumber);
            MyOrderSingleItemGrandTotal = itemView.findViewById(R.id.MyOrderSingleItemGrandTotal);
            MyOrderSingleItemOrderStatus = itemView.findViewById(R.id.MyOrderSingleItemOrderStatus);
            MyOrderSingleItemRecyclerView = itemView.findViewById(R.id.MyOrderSingleItemRecyclerView);
            MyOrderSingleItemOrderStatusEstimateDelivery = itemView.findViewById(R.id.MyOrderSingleItemOrderStatusEstimateDelivery);
            MyOrderSingleItemOrderInfo = itemView.findViewById(R.id.MyOrderSingleItemOrderInfo);
        }
    }

    public void setAdapter(List<UCart> uCartList, RecyclerView recyclerView) {
        if (uCartList != null && uCartList.size() > 0) {
            MyOrderSummaryAdapter myOrderSummaryAdapter = new MyOrderSummaryAdapter(context, uCartList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(myOrderSummaryAdapter);
        }
    }

    public void setOrderStatus(OrderStatus orderStatus, TextView MyOrderSingleItemOrderStatus) {
        switch (orderStatus) {
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
