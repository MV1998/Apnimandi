package com.mohit.varma.apnimandi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.MyOrderAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.Orders;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OngoingOrdersFragment extends Fragment {
    private static final String TAG = "OngoingOrdersFragment";
    private RecyclerView MyOrdersActivityRecyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Context context;
    private List<Orders> ordersList = new ArrayList<>();
    private MyOrderAdapter myOrderAdapter;

    public OngoingOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ongoing_orders, container, false);

        initViews(view);


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
 /*                               FruitsActivitySearchCardView.setVisibility(View.VISIBLE);
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
 /*                       FruitsActivitySearchCardView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        FruitsActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);*/
                        dismissProgressDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


    public void initViews(View view){
        MyOrdersActivityRecyclerView = view.findViewById(R.id.MyOrdersActivityRecyclerView);
        this.context = getActivity();
        databaseReference = new MyFirebaseDatabase().getReference();
        progressDialog = new ProgressDialog(context);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    public void setAdapter() {
        if (ordersList != null && ordersList.size() > 0) {
            myOrderAdapter = new MyOrderAdapter(context,ordersList,databaseReference);
            MyOrdersActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyOrdersActivityRecyclerView.setHasFixedSize(true);
            MyOrdersActivityRecyclerView.setAdapter(myOrderAdapter);
        }
    }

}
