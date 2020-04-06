package com.mohit.varma.apnimandi.activitites;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.ItemAdapter;
import com.mohit.varma.apnimandi.adapters.MyOrderAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.Orders;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersActivity extends AppCompatActivity {
    private static final String TAG = "MyOrdersActivity";
    private RecyclerView MyOrdersActivityRecyclerView;
    private Toolbar MyOrdersActivityToolbar;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Context context;
    private List<Orders> ordersList = new ArrayList<>();
    private MyOrderAdapter myOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        initViews();

        MyOrdersActivityToolbar.setNavigationOnClickListener(navigation->{
            onBackPressed();
        });

        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ordersList.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot Items : dataSnapshot.getChildren()) {
                            Orders uItem = Items.getValue(Orders.class);
                            ordersList.add(uItem);
                        }
                        dismissProgressDialog();
                        if(ordersList != null && ordersList.size()>0){
                            Log.d(TAG, "onDataChange: " + new Gson().toJson(ordersList));
                        }
                        if (ordersList != null && ordersList.size() > 0) {
                            if (myOrderAdapter != null) {
                                dismissProgressDialog();
/*                                FruitsActivitySearchCardView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                FruitsActivityNoItemAddedYetTextView.setVisibility(View.GONE);*/
                                myOrderAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
/*                                FruitsActivitySearchCardView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                FruitsActivityNoItemAddedYetTextView.setVisibility(View.GONE);*/
                                setAdapter();
                            }
                        }
                    } else {
/*                        FruitsActivitySearchCardView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        FruitsActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
                        dismissProgressDialog();*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void initViews(){
        MyOrdersActivityToolbar = findViewById(R.id.MyOrdersActivityToolbar);
        MyOrdersActivityRecyclerView = findViewById(R.id.MyOrdersActivityRecyclerView);
        this.context = this;
        databaseReference = new MyFirebaseDatabase().getReference();
        progressDialog = new ProgressDialog(context);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    public void setAdapter() {
        if (ordersList != null && ordersList.size() > 0) {
            myOrderAdapter = new MyOrderAdapter(context,ordersList);
            MyOrdersActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyOrdersActivityRecyclerView.setHasFixedSize(true);
            MyOrdersActivityRecyclerView.setAdapter(myOrderAdapter);
        }
    }
}
