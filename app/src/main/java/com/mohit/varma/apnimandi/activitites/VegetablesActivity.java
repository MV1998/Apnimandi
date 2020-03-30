package com.mohit.varma.apnimandi.activitites;

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
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.LinkedList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.ITEMS;
import static com.mohit.varma.apnimandi.utilites.Constants.ITEM_KEY;

public class VegetablesActivity extends AppCompatActivity {
    public static final String TAG = VegetablesActivity.class.getCanonicalName();

    private Toolbar VegetablesActivityToolbar;
    private RecyclerView VegetablesActivityRecyclerView;
    private TextView VegetablesActivityNoItemAddedYetTextView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private ItemAdapter itemVegetablesAdapter;
    private ProgressDialog progressDialog;
    private View VegetablesActivityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagetables);

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
                            if (itemVegetablesAdapter != null) {
                                dismissProgressDialog();
                                VegetablesActivityRecyclerView.setVisibility(View.VISIBLE);
                                VegetablesActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                itemVegetablesAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                VegetablesActivityRecyclerView.setVisibility(View.VISIBLE);
                                VegetablesActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    } else {
                        VegetablesActivityRecyclerView.setVisibility(View.GONE);
                        VegetablesActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
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

        VegetablesActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void initViewsAndInstances() {
        VegetablesActivityToolbar = (Toolbar) findViewById(R.id.VegetablesActivityToolbar);
        VegetablesActivityRecyclerView = (RecyclerView) findViewById(R.id.VegetablesActivityRecyclerView);
        VegetablesActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.VegetablesActivityNoItemAddedYetTextView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        VegetablesActivityRootView = (View) findViewById(R.id.VegetablesActivityRootView);
        this.context = this;
        progressDialog = new ProgressDialog(context);
    }

    public void setToolbar() {
        setSupportActionBar(VegetablesActivityToolbar);
        VegetablesActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context, R.color.white)));
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
            itemVegetablesAdapter = new ItemAdapter(uItemList, context,category,VegetablesActivityRootView);
            VegetablesActivityRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            VegetablesActivityRecyclerView.setHasFixedSize(true);
            VegetablesActivityRecyclerView.setAdapter(itemVegetablesAdapter);
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
                    ShowSnackBar.snackBar(context, VegetablesActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
        }
        return true;
    }
}