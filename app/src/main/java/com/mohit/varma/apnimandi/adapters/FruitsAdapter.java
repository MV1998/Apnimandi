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
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.SQLiteDataBaseConnect;
import com.mohit.varma.apnimandi.model.Item;

import java.util.ArrayList;

public class FruitsAdapter extends RecyclerView.Adapter<FruitsAdapter.FruitsViewHolder> {

    private ArrayList<Item> items;
    private Context context;

    public FruitsAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public FruitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview,parent,false);
        FruitsViewHolder fruitsViewHolder = new FruitsViewHolder(view);
        return fruitsViewHolder;
    }

    @Override
    public void onBindViewHolder(FruitsViewHolder holder, final int position) {
        Item item = items.get(position);
        holder.percentoff.setText(item.getPercentOff());
        holder.nameoffood.setText(item.getItemName());
        holder.priceoffood.setText(item.getItemPrice());
        holder.imageView.setImageResource(item.getImageResourceId());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new SQLiteDataBaseConnect(context).AdditemtoDatabase(items.get(position))){
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class FruitsViewHolder extends RecyclerView.ViewHolder{


        private TextView percentoff,nameoffood,priceoffood;
        private ImageView imageView;
        private Button button;


        public FruitsViewHolder(@NonNull View itemView) {
            super(itemView);


            percentoff = itemView.findViewById(R.id.percentOff);
            nameoffood = itemView.findViewById(R.id.itemName);
            priceoffood = itemView.findViewById(R.id.priceofitem);
            imageView = itemView.findViewById(R.id.imageOffruits);
            button = itemView.findViewById(R.id.buttontoadd);
        }
    }
}



