package com.mohit.varma.apnimandi.activitites;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.fragments.OngoingOrdersFragment;
import com.mohit.varma.apnimandi.fragments.PastOrdersFragment;
import com.mohit.varma.apnimandi.model.OrderStatus;
import com.mohit.varma.apnimandi.model.Orders;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "MyOrdersActivity";
    private Toolbar MyOrdersActivityToolbar;
    private TextView MyOrdersActivityOngoingOrdersTextView, MyOrdersActivityPastOrdersTextView;
    private SwipeRefreshLayout MyOrdersActivitySwipeRefreshLayout;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private Context context;
    private DatabaseReference databaseReference;
    private List<Orders> ordersList = new ArrayList<>();
    private List<Orders> mainOrdersList = new LinkedList<>();
    private List<Orders> sortOrderList;
    private List<Orders> cancelledOrders;
    private List<Orders> processingOrders;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Bundle bundle;
    private View MyOrdersActivityRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        initViews();
        showProgressDialog();
        MyOrdersActivityToolbar.setNavigationOnClickListener(navigation -> {
            onBackPressed();
        });
        setListener();
        MyOrdersActivitySwipeRefreshLayout.setOnRefreshListener(this);

        if (firebaseAuth != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("MyOrders").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                Log.d(TAG, "onDataChange: ");
                                ordersList.clear();
                                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                    for (DataSnapshot Items : dataSnapshot.getChildren()) {
                                        Orders uItem = Items.getValue(Orders.class);
                                        ordersList.add(uItem);
                                    }
                                    dismissProgressDialog();
                                } else {
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
                    databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mainOrdersList.clear();
                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                for (DataSnapshot Items : dataSnapshot.getChildren()) {
                                    Orders orders = Items.getValue(Orders.class);
                                    mainOrdersList.add(orders);
                                }
                                if (ordersList != null && ordersList.size() > 0) {
                                    if (mainOrdersList != null && mainOrdersList.size() > 0) {
                                        sortOrderList = new LinkedList<>();
                                        for (int i = 0; i <ordersList.size(); i++) {
                                            for (int j = 0; j <mainOrdersList.size(); j++) {
                                                if(ordersList.get(i).getOrderId() == mainOrdersList.get(j).getOrderId()){
                                                    sortOrderList.add(mainOrdersList.get(j));
                                                }
                                            }
                                        }
                                    }
                                    cancelledOrders = new ArrayList<>();
                                    processingOrders = new ArrayList<>();
                                    for (int i = 0; i < sortOrderList.size(); i++) {
                                        if (sortOrderList.get(i).getOrderStatus() == OrderStatus.CANCELLED ||sortOrderList.get(i).getOrderStatus() == OrderStatus.DELIVERED) {
                                            cancelledOrders.add(sortOrderList.get(i));
                                        } else {
                                            processingOrders.add(sortOrderList.get(i));
                                        }
                                    }

                                    dismissProgressDialog();
                                    initializeOnGoingFragment();
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

    public void initViews() {
        MyOrdersActivityOngoingOrdersTextView = findViewById(R.id.MyOrdersActivityOngoingOrdersTextView);
        MyOrdersActivityPastOrdersTextView = findViewById(R.id.MyOrdersActivityPastOrdersTextView);
        MyOrdersActivitySwipeRefreshLayout = findViewById(R.id.MyOrdersActivitySwipeRefreshLayout);
        MyOrdersActivityToolbar = findViewById(R.id.MyOrdersActivityToolbar);
        MyOrdersActivityRootView = findViewById(R.id.MyOrdersActivityRootView);
        this.databaseReference = new MyFirebaseDatabase().getReference();
        this.firebaseAuth = MyApplication.getFirebaseAuth();
        this.context = this;
        this.bundle = new Bundle();
        this.progressDialog = new ProgressDialog(context);
    }

    public void setListener() {
        MyOrdersActivityOngoingOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsInternetConnectivity.isConnected(context)) {
                    try {
                        MyOrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
                        MyOrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#000000"));
                    } catch (Exception e) {

                    }
                    fragment = null;
                    fragment = new OngoingOrdersFragment();
                    bundle.putSerializable("processingOrder", (Serializable) processingOrders);
                    fragment.setArguments(bundle);
                    if (fragment != null) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.OrdersContainer, fragment, "OngoingOrdersFragment");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                } else {
                    ShowSnackBar.snackBar(context, MyOrdersActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

        MyOrdersActivityPastOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsInternetConnectivity.isConnected(context)) {
                    try {
                        MyOrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#000000"));
                        MyOrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
                    } catch (Exception e) {

                    }
                    fragment = null;
                    fragment = new PastOrdersFragment();
                    bundle.putSerializable("cancelledOrder", (Serializable) cancelledOrders);
                    fragment.setArguments(bundle);
                    if (fragment != null) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.OrdersContainer, fragment, "PastOrdersFragment");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                } else {
                    ShowSnackBar.snackBar(context, MyOrdersActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
    }

    public void initializeOnGoingFragment() {
        try {
            MyOrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
            MyOrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#000000"));
        } catch (Exception e) {

        }
        fragment = null;
        fragment = new OngoingOrdersFragment();
        bundle.putSerializable("processingOrder", (Serializable) processingOrders);
        fragment.setArguments(bundle);
        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.OrdersContainer, fragment, "OngoingOrdersFragment");
            fragmentTransaction.commitAllowingStateLoss();
            if(MyOrdersActivitySwipeRefreshLayout.isRefreshing()){
                MyOrdersActivitySwipeRefreshLayout.setRefreshing(false);
            }
        }
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

    @Override
    public void onRefresh() {
        MyOrdersActivitySwipeRefreshLayout.setRefreshing(true);
        initializeOnGoingFragment();
    }
}
