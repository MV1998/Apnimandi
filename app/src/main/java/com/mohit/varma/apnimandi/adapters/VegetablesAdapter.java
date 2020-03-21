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

public class VegetablesAdapter extends RecyclerView.Adapter<VegetablesAdapter.VegetableViewHolder>{

    private Context context;
    private ArrayList<Item> vegetable_item_list;

    public VegetablesAdapter(Context context, ArrayList<Item> vegetable_item_list) {
        this.context = context;
        this.vegetable_item_list = vegetable_item_list;
    }

    @Override
    public VegetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview,parent,false);
        VegetableViewHolder vegetableViewHolder = new VegetableViewHolder(view);
        return vegetableViewHolder;
    }

    @Override
    public void onBindViewHolder(VegetableViewHolder holder,final int position) {
        Item item = vegetable_item_list.get(position);
        holder.percentoff.setText(item.getPercentOff());
        holder.nameoffood.setText(item.getItemName());
        holder.priceoffood.setText(item.getItemPrice());
        holder.imageView.setImageResource(item.getImageResourceId());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new SQLiteDataBaseConnect(context).AdditemtoDatabase(vegetable_item_list.get(position))){
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vegetable_item_list.size();
    }

    public class VegetableViewHolder extends RecyclerView.ViewHolder{

        private TextView percentoff,nameoffood,priceoffood;
        private ImageView imageView;
        private Button button;

        public VegetableViewHolder(@NonNull View itemView) {
            super(itemView);
            percentoff = itemView.findViewById(R.id.percentOff);
            nameoffood = itemView.findViewById(R.id.itemName);
            priceoffood = itemView.findViewById(R.id.priceofitem);
            imageView = itemView.findViewById(R.id.imageOffruits);
            button = itemView.findViewById(R.id.buttontoadd);
        }
    }
}