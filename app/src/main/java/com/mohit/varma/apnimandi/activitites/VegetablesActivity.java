package com.mohit.varma.apnimandi.activitites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
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

public class VegetablesActivity extends AppCompatActivity {
    public static final String TAG = VegetablesActivity.class.getCanonicalName();

    private Toolbar VegetablesActivityToolbar;
    private RecyclerView VegetablesActivityRecyclerView;
    private SearchView VegetablesActivitySearchView;
    private CardView VegetablesActivitySearchCardView;
    private TextView VegetablesActivityNoItemAddedYetTextView,VegetablesActivityQueryHint,vegetableLinearLayoutGoToCartTextView,vegetableLinearLayoutOrderNowTextView;
    private LinearLayout vegetableLinearLayout;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private List<UCart> uCartList;
    private ItemAdapter itemVegetablesAdapter;
    private ProgressDialog progressDialog;
    private View VegetablesActivityRootView;
    private Session session;

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

        uCartList = session.getUCartList();

        if(uCartList != null && uCartList.size()>0){
            vegetableLinearLayout.setVisibility(View.VISIBLE);
            Log.d(TAG, "onCreate: " + new Gson().toJson(uCartList));
        }else {
            vegetableLinearLayout.setVisibility(View.GONE);
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
                                VegetablesActivitySearchCardView.setVisibility(View.VISIBLE);
                                VegetablesActivityRecyclerView.setVisibility(View.VISIBLE);
                                VegetablesActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                itemVegetablesAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                VegetablesActivitySearchCardView.setVisibility(View.VISIBLE);
                                VegetablesActivityRecyclerView.setVisibility(View.VISIBLE);
                                VegetablesActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                            }
                        }
                    } else {
                        VegetablesActivitySearchCardView.setVisibility(View.GONE);
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

        VegetablesActivitySearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                VegetablesActivityQueryHint.setVisibility(View.VISIBLE);
                return false;
            }
        });

        VegetablesActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        VegetablesActivitySearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VegetablesActivityQueryHint.setVisibility(View.GONE);
            }
        });
        VegetablesActivitySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                itemVegetablesAdapter.filter(text);
                return false;
            }
        });

        vegetableLinearLayoutGoToCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddToCartActivity.class);
                startActivity(intent);
            }
        });

        vegetableLinearLayoutOrderNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initViewsAndInstances() {
        VegetablesActivityToolbar = (Toolbar) findViewById(R.id.VegetablesActivityToolbar);
        VegetablesActivityRecyclerView = (RecyclerView) findViewById(R.id.VegetablesActivityRecyclerView);
        VegetablesActivityNoItemAddedYetTextView = (TextView) findViewById(R.id.VegetablesActivityNoItemAddedYetTextView);
        vegetableLinearLayout = findViewById(R.id.vegetableLinearLayout);
        vegetableLinearLayoutGoToCartTextView = findViewById(R.id.vegetableLinearLayoutGoToCartTextView);
        vegetableLinearLayoutOrderNowTextView = findViewById(R.id.vegetableLinearLayoutOrderNowTextView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        VegetablesActivitySearchView = findViewById(R.id.VegetablesActivitySearchView);
        VegetablesActivityQueryHint = findViewById(R.id.VegetablesActivityQueryHint);
        VegetablesActivityRootView = (View) findViewById(R.id.VegetablesActivityRootView);
        VegetablesActivitySearchCardView = findViewById(R.id.VegetablesActivitySearchCardView);
        this.context = this;
        progressDialog = new ProgressDialog(context);
        this.session = new Session(context);
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
            itemVegetablesAdapter = new ItemAdapter(uItemList, context, category, VegetablesActivityRootView, new ItemClickCallBack() {
                @Override
                public void clickCallBack() {
                    if(vegetableLinearLayout.getVisibility() == View.VISIBLE){

                    }else {
                        vegetableLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
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