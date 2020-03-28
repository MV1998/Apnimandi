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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.AddToCardAdapter;
import com.mohit.varma.apnimandi.adapters.ItemAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.Item;
import com.mohit.varma.apnimandi.model.UCart;

import java.util.LinkedList;
import java.util.List;

public class AddToCartActivity extends AppCompatActivity {
    public static final String TAG = AddToCartActivity.class.getSimpleName();
    private RecyclerView AddToCartActivityRecyclerView;
    private AddToCardAdapter addToCardAdapter;
    private Toolbar AddToCartActivityToolBar;
    private TextView AddToCartActivityNoItemAddedYetTextView;
    private View AddToCartActivityRootView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private List<UCart> uCartList = new LinkedList<>();
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        initViews();
        showProgressDialog();

        setToolBar(AddToCartActivityToolBar);
/*
        items = new SQLiteDataBaseConnect(this).getitemarraylist();

        addToCardAdapter = new AddToCardAdapter(this, items);
        recyclerView = findViewById(R.id.addtocardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addToCardAdapter);
        addToCardAdapter.notifyDataSetChanged();*/

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
                                                    Log.d(TAG, "onDataChange: " + new Gson().toJson(uCartList));
                                                    if (addToCardAdapter != null) {
                                                        dismissProgressDialog();
                                                        AddToCartActivityRecyclerView.setVisibility(View.VISIBLE);
                                                        AddToCartActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                                        addToCardAdapter.notifyDataSetChanged();
                                                    } else {
                                                        dismissProgressDialog();
                                                        AddToCartActivityRecyclerView.setVisibility(View.VISIBLE);
                                                        AddToCartActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                                        setAdapter();
                                                    }
                                                }
                                            } else {
                                                AddToCartActivityRecyclerView.setVisibility(View.GONE);
                                                AddToCartActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
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
        AddToCartActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.AddToCartActivityNoItemAddedYetTextView);
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
            AddToCartActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            AddToCartActivityRecyclerView.setHasFixedSize(true);
            AddToCartActivityRecyclerView.setAdapter(addToCardAdapter);
        }
    }
}
