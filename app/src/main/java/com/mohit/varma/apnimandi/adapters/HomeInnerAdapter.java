package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.SQLiteDataBaseConnect;
import com.mohit.varma.apnimandi.model.Item;
import com.mohit.varma.apnimandi.model.UItem;

import java.util.ArrayList;
import java.util.List;

public class HomeInnerAdapter extends RecyclerView.Adapter<HomeInnerAdapter.CustomViewHolder> {

    private Context context;
    private List<UItem> uItemList;

    public HomeInnerAdapter(Context context, List<UItem> uItemList) {
        this.context = context;
        this.uItemList = uItemList;
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
