package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.fragments.OngoingOrdersFragment;
import com.mohit.varma.apnimandi.fragments.PastOrdersFragment;

public class MyOrdersActivity extends AppCompatActivity {
    private static final String TAG = "MyOrdersActivity";
    private Toolbar MyOrdersActivityToolbar;
    private TextView MyOrdersActivityOngoingOrdersTextView,MyOrdersActivityPastOrdersTextView;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        initViews();
        MyOrdersActivityToolbar.setNavigationOnClickListener(navigation -> {
            onBackPressed();
        });
        initializeOnGoingFragment();
        setListener();
    }

    public void initViews() {
        MyOrdersActivityOngoingOrdersTextView = findViewById(R.id.MyOrdersActivityOngoingOrdersTextView);
        MyOrdersActivityPastOrdersTextView = findViewById(R.id.MyOrdersActivityPastOrdersTextView);
        MyOrdersActivityToolbar = findViewById(R.id.MyOrdersActivityToolbar);
        this.context = this;
    }

    public void setListener() {
        MyOrdersActivityOngoingOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyOrdersActivityOngoingOrdersTextView.setTextColor(context.getColor(R.color.colorPrimary));
                    MyOrdersActivityPastOrdersTextView.setTextColor(context.getColor(R.color.black));
                }catch (Exception e){

                }
                fragment = null;
                fragment = new OngoingOrdersFragment();
                if (fragment != null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.OrdersContainer, fragment, "OngoingOrdersFragment");
                    fragmentTransaction.commit();
                }
            }
        });

        MyOrdersActivityPastOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyOrdersActivityOngoingOrdersTextView.setTextColor(context.getColor(R.color.black));
                    MyOrdersActivityPastOrdersTextView.setTextColor(context.getColor(R.color.colorPrimary));
                }catch (Exception e){

                }
                fragment = null;
                fragment = new PastOrdersFragment();
                if (fragment != null) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.OrdersContainer, fragment, "PastOrdersFragment");
                    fragmentTransaction.commit();
                }
            }
        });
    }

    public void initializeOnGoingFragment(){
        try {
            MyOrdersActivityOngoingOrdersTextView.setTextColor(context.getColor(R.color.colorPrimary));
            MyOrdersActivityPastOrdersTextView.setTextColor(context.getColor(R.color.black));
        }catch (Exception e){

        }
        fragment = null;
        fragment = new OngoingOrdersFragment();
        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.OrdersContainer, fragment, "OngoingOrdersFragment");
            fragmentTransaction.commit();
        }
    }
}
