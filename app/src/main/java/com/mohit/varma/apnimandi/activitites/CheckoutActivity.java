package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.SummaryItemAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.OrderStatus;
import com.mohit.varma.apnimandi.model.Orders;
import com.mohit.varma.apnimandi.model.PaymentMethod;
import com.mohit.varma.apnimandi.model.PaymentStatus;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UserAddress;
import com.mohit.varma.apnimandi.utilites.Constants;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.Session;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.REQUEST_CODE_FOR_ADDRESS;
import static com.mohit.varma.apnimandi.utilites.Constants.USER_ADDRESS_KEY;
import static com.mohit.varma.apnimandi.utilites.Constants.generateUniqueId;

public class CheckoutActivity extends AppCompatActivity {
    public static final String TAG = CheckoutActivity.class.getSimpleName();
    private Toolbar CheckoutActivityToolBar;
    private TextView CheckoutActivityAddChangeAddress, CheckoutActivityUserName, CheckoutActivityAddressLine1,
            CheckoutActivityAddressLine2, CheckoutActivityCityCode, CheckoutActivityPhoneNumber,BottomSheetRelativeLayoutOrderIdTextView;
    private MaterialButton CheckoutActivityPlaceOrderButton;
    private View CheckoutActivityRootView, bottomSheetView;
    private RecyclerView CheckoutActivityRecyclerView;
    private BottomSheetBehavior bottomSheetBehavior;
    private List<UCart> uCartList = new ArrayList<>();
    private SummaryItemAdapter summaryItemAdapter;
    private Context context;
    private Session session;
    private long grandTotal;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private UserAddress userAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();

        userAddress = session.getAddress();
        if (userAddress != null) {
            setVisibleAddressPanel();
            CheckoutActivityUserName.setText(userAddress.getUserName());
            CheckoutActivityAddressLine1.setText(userAddress.getAddressLine1());
            CheckoutActivityAddressLine2.setText(userAddress.getAddressLine2());
            CheckoutActivityCityCode.setText(userAddress.getCityCode());
            CheckoutActivityPhoneNumber.setText(userAddress.getPhoneNumber());
        }

        /*if (getIntent().getSerializableExtra("UCartItem") != null && getIntent().getSerializableExtra("UCartGrandTotalPrice") != null) {
            //uCartList = (List<UCart>) getIntent().getSerializableExtra("UCartItem");

        }*/

        uCartList = session.getUCartList();
        Log.d(TAG, "onCreate: " + new Gson().toJson(uCartList));
 /*       grandTotal = getIntent().getLongExtra("UCartGrandTotalPrice", 0);*/
        grandTotal = session.getGrandTotal();
        if (uCartList != null) {
            Log.d(TAG, "onCreate: " + new Gson().toJson(uCartList));
            Log.d(TAG, "onCreate: " + grandTotal);
            CheckoutActivityPlaceOrderButton.setText(context.getResources().getString(R.string.confirm_order) + " \u20B9" + grandTotal);
            setAdapter();
        }

        CheckoutActivityToolBar.setNavigationOnClickListener(v -> onBackPressed());

        CheckoutActivityAddChangeAddress.setOnClickListener(v -> {
            if (IsInternetConnectivity.isConnected(context)) {
                Intent intent = new Intent(context, AddAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_ADDRESS);
            } else {
                ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
            }
        });

        CheckoutActivityPlaceOrderButton.setOnClickListener(v -> {
            if (IsInternetConnectivity.isConnected(context)) {
                if (userAddress != null) {
                    session.setAddress(userAddress);
                    if (firebaseAuth != null) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                                if (databaseReference != null) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                            int UniqueId = generateUniqueId();
                                            Orders orders = null;
                                            try {
                                                orders = new Orders(UniqueId, Constants.getLocalDate(),"", userAddress, uCartList, OrderStatus.ORDER_PLACED, PaymentMethod.CASH_ON_DELIVERY, PaymentStatus.UNPAID, grandTotal);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            if (orders != null) {
                                                Log.d(TAG, "onClick: " + new Gson().toJson(orders));
                                                databaseReference.child("Orders")
                                                        .push()
                                                        .setValue(orders)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.order_placed));
                                                                BottomSheetRelativeLayoutOrderIdTextView.setText("Your Order Number: "+UniqueId);
                                                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                                            }
                                                        });
                                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber())
                                                        .child("MyOrders")
                                                        .push()
                                                        .setValue(orders).addOnSuccessListener(aVoid -> {
                                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber())
                                                            .child("UCart")
                                                            .removeValue((var1, var2) -> {

                                                            });
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.please_enter_address));
                }
            } else {
                ShowSnackBar.snackBar(context, CheckoutActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Intent intent = new Intent(context,MyOrdersActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
    }

    public void initViews() {
        CheckoutActivityToolBar = findViewById(R.id.CheckoutActivityToolBar);
        CheckoutActivityAddChangeAddress = findViewById(R.id.CheckoutActivityAddChangeAddress);
        CheckoutActivityUserName = findViewById(R.id.CheckoutActivityUserName);
        CheckoutActivityAddressLine1 = findViewById(R.id.CheckoutActivityAddressLine1);
        CheckoutActivityAddressLine2 = findViewById(R.id.CheckoutActivityAddressLine2);
        CheckoutActivityPhoneNumber = findViewById(R.id.CheckoutActivityPhoneNumber);
        CheckoutActivityCityCode = findViewById(R.id.CheckoutActivityCityCode);
        CheckoutActivityPlaceOrderButton = findViewById(R.id.CheckoutActivityPlaceOrderButton);
        CheckoutActivityRootView = findViewById(R.id.CheckoutActivityRootView);
        CheckoutActivityRecyclerView = findViewById(R.id.CheckoutActivityRecyclerView);
        bottomSheetView = findViewById(R.id.AddToCartActivityBottomRelativeLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        BottomSheetRelativeLayoutOrderIdTextView = findViewById(R.id.BottomSheetRelativeLayoutOrderIdTextView);
        context = this;
        session = new Session(context);
        databaseReference = new MyFirebaseDatabase().getReference();
        firebaseAuth = MyApplication.getFirebaseAuth();
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
