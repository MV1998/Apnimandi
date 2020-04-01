package com.mohit.varma.apnimandi.activitites;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.AddToCardAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.LinkedList;
import java.util.List;

public class AddToCartActivity extends AppCompatActivity {
    public static final String TAG = AddToCartActivity.class.getSimpleName();
    private RecyclerView AddToCartActivityRecyclerView;
    private AddToCardAdapter addToCardAdapter;
    private Toolbar AddToCartActivityToolBar;
    private TextView AddToCartActivityNoItemAddedYetTextView,AddToCartActivityBottomRelativeLayoutTotalPriceTextView,
            AddToCartActivityBottomRelativeLayoutTotalItemTextView,AddToCartActivityBottomRelativeLayoutSubTotalCountTextView,
            AddToCartActivityBottomRelativeLayoutDeliveryCountItemTextView,AddToCartActivityBottomRelativeLayoutGrandTotalCountItemTextView;
    private RelativeLayout AddToCartActivityNoItemAddedYetLinearLayout;
    private View AddToCartActivityRootView,AddToCartActivityShadowView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private List<UCart> uCartList = new LinkedList<>();
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private RelativeLayout AddToCartActivityBottomRelativeLayout;
    private MaterialButton AddToCartActivityBottomRelativeLayoutShoppingButton,AddToCartActivityBottomRelativeLayoutPlaceOrderButton;
    private long deliveryFee,subTotal,grandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        initViews();
        showProgressDialog();
        setToolBar(AddToCartActivityToolBar);
        if (firebaseAuth != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                    if (firebaseDatabase != null) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                firebaseDatabase.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        try {
                                            uCartList.clear();
                                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                                for (DataSnapshot cartItems : dataSnapshot.getChildren()) {
                                                    UCart uCart = cartItems.getValue(UCart.class);
                                                    uCartList.add(uCart);
                                                }
                                                dismissProgressDialog();
                                                if (uCartList != null && uCartList.size() > 0) {
                                                    Log.d(TAG, "onDataChange: "+ getTotalItemPriceOfAllItem(uCartList));
                                                    subTotal= getTotalItemPriceOfAllItem(uCartList);
                                                    AddToCartActivityBottomRelativeLayoutSubTotalCountTextView.setText("\u20B9"+subTotal);
                                                    grandTotal = subTotal+deliveryFee;
                                                    AddToCartActivityBottomRelativeLayoutGrandTotalCountItemTextView.setText("\u20B9"+grandTotal);
                                                    AddToCartActivityBottomRelativeLayoutTotalItemTextView.setText(uCartList.size()+" Items");
                                                    AddToCartActivityRecyclerView.setVisibility(View.VISIBLE);
                                                    AddToCartActivityNoItemAddedYetLinearLayout.setVisibility(View.GONE);
                                                    if (addToCardAdapter != null) {
                                                        dismissProgressDialog();
                                                        addToCardAdapter.notifyDataSetChanged();
                                                    } else {
                                                        dismissProgressDialog();
                                                        setAdapter();
                                                    }
                                                }else {
                                                    AddToCartActivityBottomRelativeLayout.setVisibility(View.GONE);
                                                    //AddToCartActivityShadowView.setVisibility(View.GONE);
                                                    AddToCartActivityNoItemAddedYetLinearLayout.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                AddToCartActivityRecyclerView.setVisibility(View.GONE);
/*                                                AddToCartActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);*/
                                                AddToCartActivityBottomRelativeLayout.setVisibility(View.GONE);
                                                AddToCartActivityNoItemAddedYetLinearLayout.setVisibility(View.VISIBLE);
                                                //AddToCartActivityShadowView.setVisibility(View.GONE);
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
                            }

                            firebaseDatabase.child("Admin").child("deliveryFee").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(dataSnapshot.getValue()!= null){
                                            AddToCartActivityBottomRelativeLayoutDeliveryCountItemTextView.setText("\u20B9"+dataSnapshot.getValue()+"");
                                            deliveryFee = (long) dataSnapshot.getValue();
                                            grandTotal = subTotal + deliveryFee;
                                            AddToCartActivityBottomRelativeLayoutGrandTotalCountItemTextView.setText("\u20B9"+grandTotal);
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
        }

        AddToCartActivityToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AddToCartActivityBottomRelativeLayoutShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IsInternetConnectivity.isConnected(context)){
                    onBackPressed();
                }else {
                    ShowSnackBar.snackBar(context,AddToCartActivityRootView,context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

        AddToCartActivityBottomRelativeLayoutPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IsInternetConnectivity.isConnected(context)){
                    ShowSnackBar.snackBar(context,AddToCartActivityRootView,context.getResources().getString(R.string.mohit_working_on_it));
                }else {
                    ShowSnackBar.snackBar(context,AddToCartActivityRootView,context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

    }

    public void setToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Cart");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    public void initViews() {
        AddToCartActivityRecyclerView = (RecyclerView) findViewById(R.id.AddToCartActivityRecyclerView);
        AddToCartActivityToolBar = (Toolbar) findViewById(R.id.AddToCartActivityToolBar);
        AddToCartActivityRootView = (View) findViewById(R.id.AddToCartActivityRootView);
        AddToCartActivityBottomRelativeLayout = (RelativeLayout) findViewById(R.id.AddToCartActivityBottomRelativeLayout);
        AddToCartActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.AddToCartActivityNoItemAddedYetTextView);
        AddToCartActivityBottomRelativeLayoutTotalPriceTextView = (TextView) findViewById(R.id.AddToCartActivityBottomRelativeLayoutTotalPriceTextView);
        AddToCartActivityBottomRelativeLayoutTotalItemTextView =(TextView) findViewById(R.id.AddToCartActivityBottomRelativeLayoutTotalItemTextView);
        AddToCartActivityBottomRelativeLayoutShoppingButton = (MaterialButton) findViewById(R.id.AddToCartActivityBottomRelativeLayoutShoppingButton);
        AddToCartActivityBottomRelativeLayoutPlaceOrderButton = (MaterialButton) findViewById(R.id.AddToCartActivityBottomRelativeLayoutPlaceOrderButton);

        AddToCartActivityBottomRelativeLayoutSubTotalCountTextView = (TextView) findViewById(R.id.AddToCartActivityBottomRelativeLayoutSubTotalCountTextView);
        AddToCartActivityBottomRelativeLayoutDeliveryCountItemTextView = (TextView) findViewById(R.id.AddToCartActivityBottomRelativeLayoutDeliveryCountItemTextView);
        AddToCartActivityBottomRelativeLayoutGrandTotalCountItemTextView = (TextView) findViewById(R.id.AddToCartActivityBottomRelativeLayoutGrandTotalCountItemTextView);
        AddToCartActivityNoItemAddedYetLinearLayout = (RelativeLayout) findViewById(R.id.AddToCartActivityNoItemAddedYetLinearLayout);
        //AddToCartActivityShadowView = (View) findViewById(R.id.AddToCartActivityShadowView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        firebaseAuth = MyApplication.getFirebaseAuth();
        this.context = this;
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
        if (uCartList != null && uCartList.size() > 0) {
            addToCardAdapter = new AddToCardAdapter(context,uCartList,firebaseDatabase,firebaseAuth,AddToCartActivityRootView);
            AddToCartActivityRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
            AddToCartActivityRecyclerView.setHasFixedSize(true);
            AddToCartActivityRecyclerView.setAdapter(addToCardAdapter);
        }
    }

    public int getTotalItemPriceOfAllItem(List<UCart> uCartList){
        int total =0;
        for(UCart uCart : uCartList){
            total = total+uCart.getmItemFinalPrice();
        }
        return total;
    }
}
