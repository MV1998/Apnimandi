package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.activitites.RegistrationActivity;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UItem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.FruitsViewHolder> {
    public static final String TAG = ItemAdapter.class.getSimpleName();
    private List<UItem> items;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private String category;
    private DatabaseReference databaseReference;

    public ItemAdapter(List<UItem> items, Context context, String category) {
        this.items = items;
        this.context = context;
        this.category = category;
        this.firebaseAuth = MyApplication.getFirebaseAuth();
        this.databaseReference = new MyFirebaseDatabase().getReference();
    }

    @Override
    public FruitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
        return new FruitsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FruitsViewHolder holder, final int position) {
        UItem item = items.get(position);
        holder.itemCutOffPrice.setText(item.getmItemCutOffPrice() + "");
        holder.itemName.setText(item.getmItemName() + "");
        holder.itemPrice.setText(item.getmItemWeight() + " " + item.getmItemPrice());
        if (item.getmItemImage() != null && !item.getmItemImage().isEmpty()) {
            setImageToGlide(item.getmItemImage(), holder.imageView);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth != null) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                            Log.d(TAG, "onClick: " + new Gson().toJson(items.get(position)));
                            if (databaseReference != null) {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                        final UCart[] uCart = {new UCart(item.getmItemId(), item.getmItemCutOffPrice(), item.getmItemPrice(), item.getmItemName(), item.getmItemImage(), item.getmItemWeight(), item.getmItemCategory(), item.isPopular(), 1, item.getmItemPrice())};
                                        if (uCart[0] != null) {
                                            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart").orderByChild("mItemId").equalTo(item.getmItemId()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Toast.makeText(context, "This item already added.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart")
                                                                .push().setValue(uCart[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(context, "Item added to database", Toast.LENGTH_SHORT).show();
                                                                uCart[0] = null;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(TAG, "onFailure: failed");
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
        return items.size();
    }

    public class FruitsViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCutOffPrice, itemName, itemPrice;
        private ImageView imageView;
        private Button button;

        public FruitsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCutOffPrice = itemView.findViewById(R.id.percentOff);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.priceofitem);
            imageView = itemView.findViewById(R.id.imageOffruits);
            button = itemView.findViewById(R.id.buttontoadd);
        }
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
    }
}



