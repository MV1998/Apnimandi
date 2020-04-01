package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.model.UItemDescription;
import com.mohit.varma.apnimandi.serializables.SerializableUCart;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.ArrayList;
import java.util.List;

public class ItemDescriptionActivity extends AppCompatActivity {
    public static final String TAG = ItemDescriptionActivity.class.getSimpleName();
    private ImageView ItemDescriptionActivityItemImageView;
    private Toolbar ItemDescriptionActivityToolbar;
    private TextView ItemDescriptionActivityItemName, ItemDescriptionActivityItemPrice,ItemDescriptionActivityDetailTextView,
            ItemDescriptionActivityCaloriesTextView,ItemDescriptionActivityFatTextView,ItemDescriptionActivityProteinTextView;
    private MaterialButton ItemDescriptionActivityBottomRelativeLayoutPlaceOrderButton;
    private View ItemDescriptionActivityRootView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private UItem item;
    private SerializableUCart serializableUCart;
    private Context context;
    private List<UCart> uCartList = new ArrayList<>();
    private UItemDescription uItemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        initViews();
        getAllUCartItems();

        if (getIntent().getSerializableExtra("current_item") != null) {
            serializableUCart = (SerializableUCart) getIntent().getSerializableExtra("current_item");
            if (serializableUCart != null) {
                item = serializableUCart.getuItem();
            }
        }

        if (item != null) {
            Log.d(TAG, "onCreate: " + new Gson().toJson(item));
            setImageToGlide(item.getmItemImage(), ItemDescriptionActivityItemImageView);
            ItemDescriptionActivityItemName.setText(""+item.getmItemName());
            ItemDescriptionActivityItemPrice.setText("\u20B9"+item.getmItemPrice()+"/kg");
            uItemDescription = item.getuItemDescription();
            if(uItemDescription != null){
                ItemDescriptionActivityDetailTextView.setText(uItemDescription.getItemDescription());
                ItemDescriptionActivityCaloriesTextView.setText(uItemDescription.getItemCalories());
                ItemDescriptionActivityFatTextView.setText(uItemDescription.getItemFat());
                ItemDescriptionActivityProteinTextView.setText(uItemDescription.getItemProtein());
            }
        }

        ItemDescriptionActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ItemDescriptionActivityBottomRelativeLayoutPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(context)) {
                    if (firebaseAuth != null) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                                if (databaseReference != null) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                            if (!UCartItemAlreadyExists(item.getmItemId())) {
                                                final UCart uCart = new UCart(item.getmItemId(), item.getmItemCutOffPrice(), item.getmItemPrice(), item.getmItemName(), item.getmItemImage(), item.getmItemWeight(), item.getmItemCategory(), item.isPopular(), 1, item.getmItemPrice());
                                                if (uCart != null) {
                                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart")
                                                            .push().setValue(uCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            ShowSnackBar.snackBar(context, ItemDescriptionActivityRootView, context.getResources().getString(R.string.item_added_to_cart));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            ShowSnackBar.snackBar(context, ItemDescriptionActivityRootView, context.getResources().getString(R.string.failed));
                                                        }
                                                    });
                                                }
                                            } else {
                                                ShowSnackBar.snackBar(context, ItemDescriptionActivityRootView, context.getResources().getString(R.string.item_already_in_add_to_cart));
                                            }
                                        }
                                    }
                                }
                            } else {
                                Intent intent = new Intent(context, RegistrationActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    }
                } else {
                    ShowSnackBar.snackBar(context, ItemDescriptionActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
    }

    public void initViews() {
        ItemDescriptionActivityItemImageView = (ImageView) findViewById(R.id.ItemDescriptionActivityItemImageView);
        ItemDescriptionActivityToolbar = (Toolbar) findViewById(R.id.ItemDescriptionActivityToolbar);
        ItemDescriptionActivityBottomRelativeLayoutPlaceOrderButton = (MaterialButton) findViewById(R.id.ItemDescriptionActivityBottomRelativeLayoutPlaceOrderButton);
        ItemDescriptionActivityRootView = (View) findViewById(R.id.ItemDescriptionActivityRootView);
        ItemDescriptionActivityItemName = (TextView) findViewById(R.id.ItemDescriptionActivityItemName);
        ItemDescriptionActivityItemPrice = (TextView) findViewById(R.id.ItemDescriptionActivityItemPrice);

        ItemDescriptionActivityDetailTextView = (TextView) findViewById(R.id.ItemDescriptionActivityDetailTextView);
        ItemDescriptionActivityCaloriesTextView = (TextView) findViewById(R.id.ItemDescriptionActivityCaloriesTextView);
        ItemDescriptionActivityFatTextView = (TextView) findViewById(R.id.ItemDescriptionActivityFatTextView);
        ItemDescriptionActivityProteinTextView = (TextView) findViewById(R.id.ItemDescriptionActivityProteinTextView);

        this.context = this;
        firebaseAuth = MyApplication.getFirebaseAuth();
        databaseReference = new MyFirebaseDatabase().getReference();
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
    }

    public boolean UCartItemAlreadyExists(int id) {
        if (uCartList != null && uCartList.size() > 0) {
            for (int i = 0; i < uCartList.size(); i++) {
                if (id == uCartList.get(i).getmItemId()) {
                    return true;
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    public void getAllUCartItems() {
        if (firebaseAuth != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                    if (databaseReference != null) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        try {
                                            uCartList.clear();
                                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                                for (DataSnapshot cartItems : dataSnapshot.getChildren()) {
                                                    UCart uCart = cartItems.getValue(UCart.class);
                                                    uCartList.add(uCart);
                                                }
                                                Log.d(TAG, "onDataChange: " + uCartList.size());
                                            }
                                            if (uCartList.size() == 0)
                                                Log.d(TAG, "onDataChange: " + uCartList.size());
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
    }
}
