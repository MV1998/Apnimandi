package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.activitites.RegistrationActivity;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.ArrayList;
import java.util.List;

public class HomeInnerAdapter extends RecyclerView.Adapter<HomeInnerAdapter.CustomViewHolder> {
    public static final String TAG = HomeInnerAdapter.class.getSimpleName();
    private Context context;
    private List<UItem> uItemList;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<UCart> uCartList;
    private View rootView;

    public HomeInnerAdapter(Context context, List<UItem> uItemList, View rootView) {
        this.context = context;
        this.uItemList = uItemList;
        this.rootView = rootView;
        this.firebaseAuth = MyApplication.getFirebaseAuth();
        this.databaseReference = new MyFirebaseDatabase().getReference();
        getAllUCartItems();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_view, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        UItem item = uItemList.get(position);
        if(item.getmItemWeight().endsWith("Off")){
            holder.SingleItemViewItemCutOffPriceTextView.setText(item.getmItemCutOffPrice());
        }else {
            holder.SingleItemViewItemCutOffPriceTextView.setText(item.getmItemCutOffPrice()+"% Off");
        }
        if(item.getmItemWeight().endsWith("kg")){
            holder.SingleItemViewItemWeightTextView.setText(item.getmItemWeight());
        }else {
            holder.SingleItemViewItemWeightTextView.setText(item.getmItemWeight()+"kg");
        }
        holder.SingleItemViewItemNameTextView.setText(item.getmItemName()+"");
        holder.SingleItemViewItemFinalPriceTextView.setText(item.getmItemPrice()+"");
        if (item.getmItemImage() != null && !item.getmItemImage().isEmpty()) {
            setImageToGlide(item.getmItemImage(), holder.SingleItemViewItemImageView);
        }

        holder.SingleItemViewAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth != null) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                            if (databaseReference != null) {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                        if (!UCartItemAlreadyExists(uItemList.get(position).getmItemId())) {
                                            final UCart uCart = new UCart(item.getmItemId(), item.getmItemCutOffPrice(), item.getmItemPrice(), item.getmItemName(), item.getmItemImage(), item.getmItemWeight(), item.getmItemCategory(), item.isPopular(), 1, item.getmItemPrice());
                                            if (uCart != null) {
                                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart")
                                                        .push().setValue(uCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_added_to_cart));
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return uItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private CardView SingleItemViewRootView;
        private TextView SingleItemViewItemWeightTextView,SingleItemViewItemCutOffPriceTextView,SingleItemViewItemNameTextView,
                SingleItemViewItemFinalPriceTextView;
        private ImageView SingleItemViewItemImageView;
        private MaterialButton SingleItemViewAddToCartButton;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            SingleItemViewRootView = (CardView) itemView.findViewById(R.id.SingleItemViewRootView);
            SingleItemViewItemWeightTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemWeightTextView);
            SingleItemViewItemCutOffPriceTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemCutOffPriceTextView);
            SingleItemViewItemNameTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemNameTextView);
            SingleItemViewItemFinalPriceTextView = (TextView) itemView.findViewById(R.id.SingleItemViewItemFinalPriceTextView);
            SingleItemViewItemImageView = (ImageView) itemView.findViewById(R.id.SingleItemViewItemImageView);
            SingleItemViewAddToCartButton = (MaterialButton) itemView.findViewById(R.id.SingleItemViewAddToCartButton);
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
                                            uCartList = new ArrayList<>();
                                            if (uCartList.size() > 0) {
                                                uCartList.clear();
                                            }
                                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                                for (DataSnapshot cartItems : dataSnapshot.getChildren()) {
                                                    UCart uCart = cartItems.getValue(UCart.class);
                                                    uCartList.add(uCart);
                                                }
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

}
