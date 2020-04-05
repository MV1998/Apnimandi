package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.SummaryItemAdapter;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UserAddress;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.ArrayList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.REQUEST_CODE_FOR_ADDRESS;
import static com.mohit.varma.apnimandi.utilites.Constants.USER_ADDRESS_KEY;

public class CheckoutActivity extends AppCompatActivity {
    public static final String TAG = CheckoutActivity.class.getSimpleName();
    private Toolbar CheckoutActivityToolBar;
    private TextView CheckoutActivityAddChangeAddress, CheckoutActivityUserName, CheckoutActivityAddressLine1,
            CheckoutActivityAddressLine2, CheckoutActivityCityCode, CheckoutActivityPhoneNumber;
    private MaterialButton CheckoutActivityPlaceOrderButton;
    private View CheckoutActivityRootView;
    private RecyclerView CheckoutActivityRecyclerView;
    private List<UCart> uCartList = new ArrayList<>();
    private SummaryItemAdapter summaryItemAdapter;
    private Context context;
    private long grandTotal;
    private UserAddress userAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();

/*        intent.putExtra("UCartItem",(Serializable) uCartList);
        intent.putExtra("UCartGrandTotalPrice",grandTotal);UCartGrandTotalPrice*/

        if (getIntent().getSerializableExtra("UCartItem") != null && getIntent().getSerializableExtra("UCartGrandTotalPrice") != null) {
            uCartList = (List<UCart>) getIntent().getSerializableExtra("UCartItem");
            grandTotal = getIntent().getLongExtra("UCartGrandTotalPrice", 0);
            if (uCartList != null) {
                Log.d(TAG, "onCreate: " + new Gson().toJson(uCartList));
                Log.d(TAG, "onCreate: " + grandTotal);
                CheckoutActivityPlaceOrderButton.setText(context.getResources().getString(R.string.confirm_order) + " \u20B9" + grandTotal);
                setAdapter();
            }
        }

        CheckoutActivityToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CheckoutActivityAddChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsInternetConnectivity.isConnected(context)) {
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_FOR_ADDRESS);
                } else {
                    ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

        CheckoutActivityPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsInternetConnectivity.isConnected(context)) {
                    if (userAddress != null) {

                    }else {
                        ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.please_enter_address));
                    }
                } else {
                    ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
    }

    public void initViews() {
        CheckoutActivityToolBar = (Toolbar) findViewById(R.id.CheckoutActivityToolBar);
        CheckoutActivityAddChangeAddress = findViewById(R.id.CheckoutActivityAddChangeAddress);
        CheckoutActivityUserName = findViewById(R.id.CheckoutActivityUserName);
        CheckoutActivityAddressLine1 = findViewById(R.id.CheckoutActivityAddressLine1);
        CheckoutActivityAddressLine2 = findViewById(R.id.CheckoutActivityAddressLine2);
        CheckoutActivityPhoneNumber = findViewById(R.id.CheckoutActivityPhoneNumber);
        CheckoutActivityCityCode = findViewById(R.id.CheckoutActivityCityCode);
        CheckoutActivityPlaceOrderButton = findViewById(R.id.CheckoutActivityPlaceOrderButton);
        CheckoutActivityRootView = findViewById(R.id.CheckoutActivityRootView);
        CheckoutActivityRecyclerView = findViewById(R.id.CheckoutActivityRecyclerView);
        context = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_ADDRESS) {
            if (data != null) {
                userAddress = (UserAddress) data.getSerializableExtra(USER_ADDRESS_KEY);
                if (userAddress != null) {
                    setVisibleAddressPanel();
                    Log.d(TAG, "onActivityResult: " + new Gson().toJson(userAddress));
                    CheckoutActivityUserName.setText(userAddress.getUserName());
                    CheckoutActivityAddressLine1.setText(userAddress.getAddressLine1());
                    CheckoutActivityAddressLine2.setText(userAddress.getAddressLine2());
                    CheckoutActivityCityCode.setText(userAddress.getCityCode());
                    CheckoutActivityPhoneNumber.setText(userAddress.getPhoneNumber());
                }
            }
        }
    }

    public void setVisibleAddressPanel() {
        CheckoutActivityUserName.setVisibility(View.VISIBLE);
        CheckoutActivityAddressLine1.setVisibility(View.VISIBLE);
        CheckoutActivityAddressLine2.setVisibility(View.VISIBLE);
        CheckoutActivityCityCode.setVisibility(View.VISIBLE);
        CheckoutActivityPhoneNumber.setVisibility(View.VISIBLE);
    }


    public void setAdapter() {
        if (uCartList != null && uCartList.size() > 0) {
            summaryItemAdapter = new SummaryItemAdapter(context, uCartList);
            CheckoutActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            CheckoutActivityRecyclerView.setHasFixedSize(true);
            CheckoutActivityRecyclerView.setAdapter(summaryItemAdapter);
        }
    }
}
