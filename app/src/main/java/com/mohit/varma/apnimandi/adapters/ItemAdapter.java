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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.activitites.BakingActivity;
import com.mohit.varma.apnimandi.activitites.FruitsActivity;
import com.mohit.varma.apnimandi.activitites.ProteinActivity;
import com.mohit.varma.apnimandi.activitites.RegistrationActivity;
import com.mohit.varma.apnimandi.activitites.SnacksActivity;
import com.mohit.varma.apnimandi.activitites.VegetablesActivity;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.FruitsViewHolder> {

    private List<UItem> items;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private String category;

    public ItemAdapter(List<UItem> items, Context context,String category) {
        this.items = items;
        this.context = context;
        this.category = category;
        this.firebaseAuth = MyApplication.getFirebaseAuth();
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
                /*Item addToCartItem = new Item();
                if(new SQLiteDataBaseConnect(context).AdditemtoDatabase(items.get(position))){
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();
                }*/
                if (firebaseAuth != null) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (!firebaseAuth.getCurrentUser().isAnonymous()) {

                        } else {
                            Intent intent = new Intent(context, RegistrationActivity.class);
                            context.startActivity(intent);
                            if(category != null && !category.isEmpty()){
                                switch (category){
                                    case Constants.FRUIT:
                                        ((FruitsActivity)context).finish();
                                        break;
                                    case Constants.VEGETABLE:
                                        ((VegetablesActivity)context).finish();
                                        break;
                                    case Constants.SNACKS:
                                        ((SnacksActivity)context).finish();
                                        break;
                                    case Constants.PROTEIN:
                                        ((ProteinActivity)context).finish();
                                        break;
                                    case Constants.BACKING:
                                        ((BakingActivity)context).finish();
                                        break;
                                }
/*                                if(category.equalsIgnoreCase(Constants.FRUIT)){

                                }else if(category.equalsIgnoreCase(Constants.VEGETABLE)){

                                }else if(category.equalsIgnoreCase(Constants.SNACKS)){

                                }else if(category.equalsIgnoreCase(Constants.PROTEIN)){

                                }else if(category.equalsIgnoreCase(Constants.))*/
                            }
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



