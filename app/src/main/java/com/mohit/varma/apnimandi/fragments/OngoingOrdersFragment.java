package com.mohit.varma.apnimandi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
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
        View view = inflater.inflate(R.layout.fragment_ongoing_orders, container, false);
        if (ordersList != null) {
            ordersList.clear();
            ordersList = (List<Orders>) getArguments().getSerializable("processingOrder");
        }
        initViews(view);
        if (ordersList != null && ordersList.size() > 0) {
            if (myOrderAdapter != null) {
                myOrderAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }
        }
        return view;
    }

    public void initViews(View view) {
        MyOrdersActivityRecyclerView = view.findViewById(R.id.MyOrdersActivityRecyclerView);
        this.context = getActivity();
        databaseReference = new MyFirebaseDatabase().getReference();
    }

    public void setAdapter() {
        if (ordersList != null && ordersList.size() > 0) {
            myOrderAdapter = new MyOrderAdapter(context, ordersList, databaseReference);
            MyOrdersActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyOrdersActivityRecyclerView.setHasFixedSize(true);
            MyOrdersActivityRecyclerView.setAdapter(myOrderAdapter);
        }
    }
}
