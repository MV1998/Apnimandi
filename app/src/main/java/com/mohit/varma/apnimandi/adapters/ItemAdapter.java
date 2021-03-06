package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mohit.varma.apnimandi.activitites.ItemDescriptionActivity;
import com.mohit.varma.apnimandi.activitites.RegistrationActivity;
import com.mohit.varma.apnimandi.activitites.SingleOrderCheckoutActivity;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.interfaces.ItemClickCallBack;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.serializables.SerializableUCart;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.FruitsViewHolder> {
    public static final String TAG = ItemAdapter.class.getSimpleName();
    private List<UItem> items;
    private List<UItem> searchItems;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private String category;
    private DatabaseReference databaseReference;
    private ItemClickCallBack itemClickCallBack;
    private List<UCart> uCartList = new ArrayList<>();
    private View rootView;
    public ItemAdapter() {
    }

    public ItemAdapter(List<UItem> items, Context context, String category, View rootView,ItemClickCallBack itemClickCallBack) {
        this.items = items;
        this.searchItems = new ArrayList<>();
        this.searchItems.addAll(items);
        this.context = context;
        this.category = category;
        this.rootView = rootView;
        this.itemClickCallBack = itemClickCallBack;
        this.firebaseAuth = MyApplication.getFirebaseAuth();
        this.databaseReference = new MyFirebaseDatabase().getReference();
        getAllUCartItems();
    }

    @Override
    public FruitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_view, parent, false);
        return new FruitsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FruitsViewHolder holder, final int position) {
        UItem item = items.get(position);
        holder.SingleItemViewItemCutOffPriceTextView.setText(item.getmItemCutOffPrice() + "% Off");
        holder.SingleItemViewItemWeightTextView.setText(item.getmItemWeight());
        holder.SingleItemViewItemNameTextView.setText(item.getmItemName() + "");
        holder.SingleItemViewItemFinalPriceTextView.setText("\u20B9" + item.getmItemPrice());

        if (item.getmItemImage() != null && !item.getmItemImage().isEmpty()) {
            setImageToGlide(item.getmItemImage(), holder.SingleItemViewItemImageView);
        }

        holder.SingleItemViewRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (IsInternetConnectivity.isConnected(context)) {
                        Intent intent = new Intent(context, ItemDescriptionActivity.class);
                        SerializableUCart serializableUCart = new SerializableUCart(items.get(position));
                        intent.putExtra("current_item", (Serializable) serializableUCart);
                        context.startActivity(intent);
                    } else {
                        ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.SingleItemViewAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(context)) {
                    if (firebaseAuth != null) {
                        if (firebaseAuth.getCurrentUser() != null) {
                            if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                                if (databaseReference != null) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                            if (!UCartItemAlreadyExists(items.get(position).getmItemId())) {
                                                final UCart uCart = new UCart(item.getmItemId(), item.getmItemCutOffPrice(), item.getmItemPrice(), item.getmItemName(), item.getmItemImage(), item.getmItemWeight(), item.getmItemCategory(), item.isPopular(), 1, item.getmItemPrice());
                                                if (uCart != null) {
                                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart")
                                                            .push().setValue(uCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_added_to_cart));
                                                            if(itemClickCallBack != null){
                                                                itemClickCallBack.clickCallBack();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.failed));
                                                        }
                                                    });
                                                }
                                            } else {
                                                ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_already_in_add_to_cart));
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
                    ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

        holder.SingleItemViewOrderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "SingleItemViewOrderNowButton: " + new Gson().toJson(items.get(position)));
                    Intent intent = new Intent(context, SingleOrderCheckoutActivity.class);
                    intent.putExtra("uItem",items.get(position));
                    context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class FruitsViewHolder extends RecyclerView.ViewHolder {
        private CardView SingleItemViewRootView;
        private TextView SingleItemViewItemWeightTextView, SingleItemViewItemCutOffPriceTextView, SingleItemViewItemNameTextView,
                SingleItemViewItemFinalPriceTextView;
        private ImageView SingleItemViewItemImageView;
        private MaterialButton SingleItemViewAddToCartButton,SingleItemViewOrderNowButton;

        public FruitsViewHolder(@NonNull View itemView) {
            super(itemView);
            SingleItemViewRootView = (CardView) itemView.findViewById(R.id.SingleItemViewRootView);
            SingleItemViewItemWeightTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemWeightTextView);
            SingleItemViewItemCutOffPriceTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemCutOffPriceTextView);
            SingleItemViewItemNameTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemNameTextView);
            SingleItemViewItemFinalPriceTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemFinalPriceTextView);
            SingleItemViewItemImageView = (ImageView) itemView.findViewById(R.id.SingleItemViewItemImageView);
            SingleItemViewAddToCartButton = (MaterialButton) itemView.findViewById(R.id.SingleItemViewAddToCartButton);
            SingleItemViewOrderNowButton = (MaterialButton) itemView.findViewById(R.id.SingleItemViewOrderNowButton);

        }
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
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

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        this.items.clear();
        if (charText.length() == 0) {
            this.items.addAll(searchItems);
        } else {
            for (UItem wp : searchItems) {
                if (wp.getmItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    this.items.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}



