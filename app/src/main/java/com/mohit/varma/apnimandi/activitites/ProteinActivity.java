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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.ItemAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.interfaces.ItemClickCallBack;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.utilites.Constants;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.Session;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.LinkedList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.ITEMS;
import static com.mohit.varma.apnimandi.utilites.Constants.ITEM_KEY;

public class ProteinActivity extends AppCompatActivity {
    public static final String TAG = ProteinActivity.class.getCanonicalName();

    private Toolbar ProteinActivityToolbar;
    private RecyclerView ProteinActivityRecyclerView;
    private SearchView ProteinActivitySearchView;
    private CardView ProteinActivitySearchCardView;
    private TextView ProteinActivityNoItemAddedYetTextView, ProteinActivityQueryHint;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private ItemAdapter itemProteinAdapter;
    private ProgressDialog progressDialog;
    private View ProteinActivityRootView;
    private Session session;
    private List<UCart> uCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protein_food);

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
                            if (itemProteinAdapter != null) {
                                dismissProgressDialog();
                                ProteinActivitySearchCardView.setVisibility(View.VISIBLE);
                                ProteinActivityRecyclerView.setVisibility(View.VISIBLE);
                                ProteinActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                itemProteinAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                ProteinActivitySearchCardView.setVisibility(View.VISIBLE);
                                ProteinActivityRecyclerView.setVisibility(View.VISIBLE);
                                ProteinActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    } else {
                        ProteinActivitySearchCardView.setVisibility(View.GONE);
                        ProteinActivityRecyclerView.setVisibility(View.GONE);
                        ProteinActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
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

        ProteinActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ProteinActivitySearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ProteinActivityQueryHint.setVisibility(View.VISIBLE);
                return false;
            }
        });

        ProteinActivitySearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProteinActivityQueryHint.setVisibility(View.GONE);
            }
        });

        ProteinActivitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                itemProteinAdapter.filter(text);
                return false;
            }
        });

    }

    public void initViewsAndInstances() {
        ProteinActivityToolbar = (Toolbar) findViewById(R.id.ProteinActivityToolbar);
        ProteinActivityRecyclerView = (RecyclerView) findViewById(R.id.ProteinActivityRecyclerView);
        ProteinActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.ProteinActivityNoItemAddedYetTextView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        ProteinActivityRootView = (View) findViewById(R.id.ProteinActivityRootView);
        ProteinActivitySearchView = findViewById(R.id.ProteinActivitySearchView);
        ProteinActivitySearchCardView = findViewById(R.id.ProteinActivitySearchCardView);
        ProteinActivityQueryHint = findViewById(R.id.ProteinActivityQueryHint);
        this.context = this;
        progressDialog = new ProgressDialog(context);
        this.session = new Session(context);
    }

    public void setToolbar() {
        setSupportActionBar(ProteinActivityToolbar);
        ProteinActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context, R.color.white)));
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
            itemProteinAdapter = new ItemAdapter(uItemList, context, category, ProteinActivityRootView, new ItemClickCallBack() {
                @Override
                public void clickCallBack() {
                }
            });
            ProteinActivityRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            ProteinActivityRecyclerView.setHasFixedSize(true);
            ProteinActivityRecyclerView.setAdapter(itemProteinAdapter);
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
                    ShowSnackBar.snackBar(context, ProteinActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
        }
        return true;
    }
}