package com.mohit.varma.apnimandi.activitites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.LinkedList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.ITEMS;
import static com.mohit.varma.apnimandi.utilites.Constants.ITEM_KEY;

public class SnacksActivity extends AppCompatActivity {
    public static final String TAG = SnacksActivity.class.getCanonicalName();

    private Toolbar SnacksActivityToolbar;
    private RecyclerView SnacksActivityRecyclerView;
    private TextView SnacksActivityNoItemAddedYetTextView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private ItemAdapter itemSnacksAdapter;
    private ProgressDialog progressDialog;
    private View SnacksActivityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

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
                            if (itemSnacksAdapter != null) {
                                dismissProgressDialog();
                                SnacksActivityRecyclerView.setVisibility(View.VISIBLE);
                                SnacksActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                itemSnacksAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                SnacksActivityRecyclerView.setVisibility(View.VISIBLE);
                                SnacksActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    } else {
                        SnacksActivityRecyclerView.setVisibility(View.GONE);
                        SnacksActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
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

        SnacksActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void initViewsAndInstances() {
        SnacksActivityToolbar = (Toolbar) findViewById(R.id.SnacksActivityToolbar);
        SnacksActivityRecyclerView = (RecyclerView) findViewById(R.id.SnacksActivityRecyclerView);
        SnacksActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.SnacksActivityNoItemAddedYetTextView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        SnacksActivityRootView = (View) findViewById(R.id.SnacksActivityRootView);
        this.context = this;
        progressDialog = new ProgressDialog(context);
    }

    public void setToolbar() {
        setSupportActionBar(SnacksActivityToolbar);
        SnacksActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context, R.color.white)));
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
            itemSnacksAdapter = new ItemAdapter(uItemList, context,category,SnacksActivityRootView);
            SnacksActivityRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            SnacksActivityRecyclerView.setHasFixedSize(true);
            SnacksActivityRecyclerView.setAdapter(itemSnacksAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_for_each_category_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.EachCategoryAddToCart:
                if (IsInternetConnectivity.isConnected(context)) {
                    Intent intent = new Intent(context, AddToCartActivity.class);
                    startActivity(intent);
                } else {
                    ShowSnackBar.snackBar(context, SnacksActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
        }
        return true;
    }
}
