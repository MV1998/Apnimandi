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

import java.util.ArrayList;

public class HomeInnerAdapter extends RecyclerView.Adapter<HomeInnerAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Item> arrayList;
    private ArrayList<Item> selectedItems;
    private Bitmap bitmap;

    public HomeInnerAdapter(Context context, ArrayList<Item> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        Item item = arrayList.get(position);
        holder.percentoff.setText(item.getPercentOff());
        holder.nameoffood.setText(item.getItemName());
        holder.priceoffood.setText(item.getItemPrice());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference reference = firebaseStorage.getReferenceFromUrl("gs://apnimandi-c7058.appspot.com/image/mohit.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        Glide.with(context).load(R.drawable.first)
                .apply(new RequestOptions().circleCrop()).transition(new DrawableTransitionOptions().crossFade()).into(holder.imageView);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new SQLiteDataBaseConnect(context).AdditemtoDatabase(arrayList.get(position))) {
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

}
