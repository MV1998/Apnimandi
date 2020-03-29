package com.mohit.varma.apnimandi.activitites;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.ItemAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.LinkedList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.ITEMS;
import static com.mohit.varma.apnimandi.utilites.Constants.ITEM_KEY;

public class FruitsActivity extends AppCompatActivity {

    public static final String TAG = FruitsActivity.class.getCanonicalName();

    private Toolbar FruitsActivityToolbar;
    private RecyclerView recyclerView;
    private TextView FruitsActivityNoItemAddedYetTextView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private ItemAdapter itemFruitAdapter;
    private ProgressDialog progressDialog;
    private View FruitsActivityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);

        initViewsAndInstances();
        showProgressDialog();

        if (getIntent().getStringExtra(ITEM_KEY) != null) {
            if (!getIntent().getStringExtra(ITEM_KEY).isEmpty()) {
                category = getIntent().getStringExtra(ITEM_KEY);
            }
        }

        setToolbar();

        FruitsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseDatabase.child(ITEMS).child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    uItemList.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot Items : dataSnapshot.getChildren()) {
                            UItem uItem = Items.getValue(UItem.class);
                            uItemList.add(uItem);
                        }
                        dismissProgressDialog();
                        if (uItemList != null && uItemList.size() > 0) {
                            if (itemFruitAdapter != null) {
                                dismissProgressDialog();
                                recyclerView.setVisibility(View.VISIBLE);
                                FruitsActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                itemFruitAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                recyclerView.setVisibility(View.VISIBLE);
                                FruitsActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        FruitsActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
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

    public void initViewsAndInstances() {
        FruitsActivityToolbar = (Toolbar) findViewById(R.id.FruitsActivityToolbar);
        recyclerView = findViewById(R.id.fruitsRecyclerViewWidget);
        FruitsActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.FruitsActivityNoItemAddedYetTextView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        FruitsActivityRootView = (View) findViewById(R.id.FruitsActivityRootView);
        this.context = this;
        progressDialog = new ProgressDialog(context);
    }

    public void setToolbar() {
        setSupportActionBar(FruitsActivityToolbar);
        FruitsActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context, R.color.white)));
    }

    public void setAdapter() {
        if (uItemList != null && uItemList.size() > 0) {
            itemFruitAdapter = new ItemAdapter(uItemList, context,category,FruitsActivityRootView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(itemFruitAdapter);
        }
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
}
