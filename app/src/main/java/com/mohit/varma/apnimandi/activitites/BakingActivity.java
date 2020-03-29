package com.mohit.varma.apnimandi.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class BakingActivity extends AppCompatActivity {
    public static final String TAG = ProteinActivity.class.getCanonicalName();

    private Toolbar BakingActivityToolbar;
    private RecyclerView BakingActivityRecyclerView;
    private TextView BakingActivityNoItemAddedYetTextView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private ItemAdapter itemBackingAdapter;
    private ProgressDialog progressDialog;
    private View BakingActivityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);

        initViewsAndInstances();
        setToolbar();

        showProgressDialog();

        if (getIntent().getStringExtra(ITEM_KEY) != null) {
            if (!getIntent().getStringExtra(ITEM_KEY).isEmpty()) {
                category = getIntent().getStringExtra(ITEM_KEY);
            }
        }

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
                            if (itemBackingAdapter != null) {
                                dismissProgressDialog();
                                BakingActivityRecyclerView.setVisibility(View.VISIBLE);
                                BakingActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                itemBackingAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                BakingActivityRecyclerView.setVisibility(View.VISIBLE);
                                BakingActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    } else {
                        BakingActivityRecyclerView.setVisibility(View.GONE);
                        BakingActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
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

        BakingActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void initViewsAndInstances() {
        BakingActivityToolbar = (Toolbar) findViewById(R.id.BakingActivityToolbar);
        BakingActivityRecyclerView = (RecyclerView) findViewById(R.id.BakingActivityRecyclerView);
        BakingActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.BakingActivityNoItemAddedYetTextView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        BakingActivityRootView = (View) findViewById(R.id.BakingActivityRootView);
        this.context = this;
        progressDialog = new ProgressDialog(context);
    }

    public void setToolbar() {
        setSupportActionBar(BakingActivityToolbar);
        BakingActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context, R.color.white)));
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
        if (uItemList != null && uItemList.size() > 0) {
            itemBackingAdapter = new ItemAdapter(uItemList, context,category,BakingActivityRootView);
            BakingActivityRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            BakingActivityRecyclerView.setHasFixedSize(true);
            BakingActivityRecyclerView.setAdapter(itemBackingAdapter);
        }
    }
}