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

public class HomeInnerAdapter extends RecyclerView.Adapter<HomeInnerAdapter.CustomViewHolder> {
    public static final String TAG = HomeInnerAdapter.class.getSimpleName();
    private Context context;
    private List<UItem> uItemList;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public HomeInnerAdapter(Context context, List<UItem> uItemList) {
        this.context = context;
        this.uItemList = uItemList;
        this.firebaseAuth = MyApplication.getFirebaseAuth();
        this.databaseReference = new MyFirebaseDatabase().getReference();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        UItem item = uItemList.get(position);
        holder.percentoff.setText(item.getmItemCutOffPrice()+"");
        holder.nameoffood.setText(item.getmItemName()+"");
        holder.priceoffood.setText(item.getmItemPrice()+"");

        if(uItemList != null && uItemList.size()>0){
            setImageToGlide(uItemList.get(position).getmItemImage(),holder.imageView);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (new SQLiteDataBaseConnect(context).AdditemtoDatabase(uItemList.get(position))) {
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();
                }*/
                if (firebaseAuth != null) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                            if (databaseReference != null) {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (firebaseAuth.getCurrentUser().getPhoneNumber() != null && !firebaseAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
                                        UCart uCart = new UCart(item.getmItemId(), item.getmItemCutOffPrice(), item.getmItemPrice(), item.getmItemName(), item.getmItemImage(), item.getmItemWeight(), item.getmItemCategory(), item.isPopular(), 1, item.getmItemPrice());
                                        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart").orderByChild("mItemId").equalTo(item.getmItemId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Toast.makeText(context, "This item already added.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("UCart")
                                                            .push().setValue(uCart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(context, "Item added to database", Toast.LENGTH_SHORT).show();
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

        private TextView percentoff, nameoffood, priceoffood;
        private ImageView imageView;
        private Button button;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            percentoff = itemView.findViewById(R.id.percentOff);
            nameoffood = itemView.findViewById(R.id.itemName);
            priceoffood = itemView.findViewById(R.id.priceofitem);
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
