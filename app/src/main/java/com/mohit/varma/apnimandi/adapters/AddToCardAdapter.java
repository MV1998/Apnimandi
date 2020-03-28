package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.List;

public class AddToCardAdapter extends RecyclerView.Adapter<AddToCardAdapter.AddToCartViewHolder> {
    public static final String TAG = AddToCardAdapter.class.getSimpleName();
    private Context context;
    private List<UCart> uCartList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private UCart updatedUCartItem;
    private View rootView;

    public AddToCardAdapter(Context context, List<UCart> uCartList, DatabaseReference databaseReference,FirebaseAuth firebaseAuth,View rootView) {
        this.context = context;
        this.uCartList = uCartList;
        this.databaseReference = databaseReference;
        this.firebaseAuth = firebaseAuth;
        this.rootView = rootView;
    }

    @Override
    public AddToCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_view_for_add_to_cart, parent, false);
        return new AddToCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddToCartViewHolder holder, final int position) {
        final UCart uCart = uCartList.get(position);
        holder.SingleItemViewAddToCartItemCutOffPriceTextView.setText(uCart.getmItemCutOffPrice() + "");
        holder.SingleItemViewAddToCartItemNameTextView.setText(uCart.getmItemName() + "");
        holder.SingleItemViewAddToCartItemPriceTextView.setText(uCart.getmItemPrice() + "");
        holder.SingleItemViewAddToCartItemActualPriceTextView.setText("" + uCart.getmItemPrice());
        holder.SingleItemViewAddToCartIncrementOneByOneTextView.setText("" + uCart.getmItemPlusMinusValue());
        holder.SingleItemViewAddToCartItemFinalPriceTextView.setText(" = " + uCart.getmItemFinalPrice());
        if (uCartList != null && uCartList.size() > 0) {
            setImageToGlide(uCart.getmItemImage(), holder.SingleItemViewAddToCartItemImageView);
        }
        holder.SingleItemViewAddToCartItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUCartItem(uCartList.get(position));
                /*if (firebaseAuth != null) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                            if (databaseReference != null) {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart").orderByChild("mItemId").equalTo(uCart.getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                                    item.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                            //ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_deleted_successfully));
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                //ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_id_not_found));
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }*/
            }
        });

        holder.SingleItemViewAddToCartItemPriceDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseOneByOne(Integer.valueOf(holder.SingleItemViewAddToCartIncrementOneByOneTextView.getText().toString()), uCart.getmItemPrice(), holder.SingleItemViewAddToCartItemFinalPriceTextView, holder.SingleItemViewAddToCartIncrementOneByOneTextView,uCartList.get(position));
            }
        });
        holder.SingleItemViewAddToCartItemPriceIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseOneByOne(Integer.valueOf(holder.SingleItemViewAddToCartIncrementOneByOneTextView.getText().toString()), uCart.getmItemPrice(), holder.SingleItemViewAddToCartItemFinalPriceTextView, holder.SingleItemViewAddToCartIncrementOneByOneTextView,uCartList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return uCartList.size();
    }

    public class AddToCartViewHolder extends RecyclerView.ViewHolder {
        private Button SingleItemViewAddToCartItemPriceDecreaseButton, SingleItemViewAddToCartItemPriceIncrementButton, SingleItemViewAddToCartItemDeleteButton;
        private TextView SingleItemViewAddToCartIncrementOneByOneTextView, SingleItemViewAddToCartItemActualPriceTextView, SingleItemViewAddToCartItemFinalPriceTextView, SingleItemViewAddToCartItemNameTextView, SingleItemViewAddToCartItemPriceTextView, SingleItemViewAddToCartItemCutOffPriceTextView;
        private ImageView SingleItemViewAddToCartItemImageView;

        public AddToCartViewHolder(@NonNull View itemView) {
            super(itemView);
            SingleItemViewAddToCartItemDeleteButton = itemView.findViewById(R.id.SingleItemViewAddToCartItemDeleteButton);
            SingleItemViewAddToCartItemPriceDecreaseButton = itemView.findViewById(R.id.SingleItemViewAddToCartItemPriceDecreaseButton);
            SingleItemViewAddToCartItemPriceIncrementButton = itemView.findViewById(R.id.SingleItemViewAddToCartItemPriceIncrementButton);
            SingleItemViewAddToCartItemActualPriceTextView = itemView.findViewById(R.id.SingleItemViewAddToCartItemActualPriceTextView);
            SingleItemViewAddToCartItemFinalPriceTextView = itemView.findViewById(R.id.SingleItemViewAddToCartItemFinalPriceTextView);
            SingleItemViewAddToCartIncrementOneByOneTextView = itemView.findViewById(R.id.SingleItemViewAddToCartIncrementOneByOneTextView);
            SingleItemViewAddToCartItemNameTextView = itemView.findViewById(R.id.SingleItemViewAddToCartItemNameTextView);
            SingleItemViewAddToCartItemPriceTextView = itemView.findViewById(R.id.SingleItemViewAddToCartItemPriceTextView);
            SingleItemViewAddToCartItemCutOffPriceTextView = itemView.findViewById(R.id.SingleItemViewAddToCartItemCutOffPriceTextView);
            SingleItemViewAddToCartItemImageView = itemView.findViewById(R.id.SingleItemViewAddToCartItemImageView);
        }
    }

    public void increaseOneByOne(int multiplyBy, int itemCurrentPrice, TextView finalPriceTextView, TextView incrementOneByOneTextView,UCart uCart) {
        int afterPlus = ++multiplyBy;
        int finalPrice = itemCurrentPrice * afterPlus;
        finalPriceTextView.setText(" = " + finalPrice);
        incrementOneByOneTextView.setText("" + afterPlus);
        updateUCartItem(afterPlus,finalPrice,uCart);
    }

    public void decreaseOneByOne(int multiplyBy, int itemCurrentPrice, TextView finalPriceTextView, TextView incrementOneByOneTextView,UCart uCart) {
        if (multiplyBy > 1) {
            int afterMinus = --multiplyBy;
            int finalPrice = itemCurrentPrice * afterMinus;
            finalPriceTextView.setText(" = " + finalPrice);
            incrementOneByOneTextView.setText("" + afterMinus);
            updateUCartItem(afterMinus,finalPrice,uCart);
        } else {
            Toast.makeText(context, "Can't be decrease value by one", Toast.LENGTH_SHORT).show();
        }
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
    }

    public void deleteUCartItem(UCart uCart){
        if(IsInternetConnectivity.isConnected(context)){
            if (firebaseAuth != null) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                        if (databaseReference != null) {
                            if (firebaseAuth.getCurrentUser() != null) {
                                if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart").orderByChild("mItemId").equalTo(uCart.getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                                item.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                        //ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_deleted_successfully));
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            //ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_id_not_found));
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }else {
            ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    public void updateUCartItem(int multiplyBy,int finalPrice, UCart uCart){
        if(IsInternetConnectivity.isConnected(context)){
            if (firebaseAuth != null) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                        if (databaseReference != null) {
                            if (firebaseAuth.getCurrentUser() != null) {
                                if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                    updatedUCartItem = new UCart(uCart.getmItemId(),uCart.getmItemCutOffPrice(),uCart.getmItemPrice(),
                                            uCart.getmItemName(),uCart.getmItemImage(),uCart.getmItemWeight(),uCart.getmItemCategory(),
                                            uCart.isPopular(),multiplyBy,finalPrice);
                                    if(updatedUCartItem != null){
                                        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).orderByChild("UCart").equalTo(uCart.getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                                    item.getRef().setValue(updatedUCartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            //dismissProgressDialog();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //dismissProgressDialog();
                                                        }
                                                    });
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
        }else {
            ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }
}