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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.activitites.ProteinActivity;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.List;

public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.ItemChipViewHolder> {

    static  final  String TAG = ItemCategoryAdapter.class.getSimpleName();
    List<String> stringList;
    Context context;
    Intent intentProtein;

    public ItemCategoryAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chip_category_layout, parent, false);
        ItemChipViewHolder itemChipViewHolder = new ItemChipViewHolder(view);
        return itemChipViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemChipViewHolder holder, int position) {
        String item = stringList.get(position);
        holder.chip.setText(item);
        startActivityAtOnClick(holder, item);
    }

    private void startActivityAtOnClick(ItemChipViewHolder holder, String item) {
        holder.chip.setOnClickListener(v -> {
            Log.d(TAG, "onClick: "+item);
            switch (item){
                case "Protein shakes":
                    startProteinActivity();
                    Log.d(TAG, "onClick: this protein ac");
                    break;
                case "Atta":
                    Log.d(TAG, "onClick: this atta ac");
                    break;
                case "Salt And Sugar":
                    Log.d(TAG, "onClick: this salt and sugar ac");
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ItemChipViewHolder extends RecyclerView.ViewHolder {
        Chip chip;
        public ItemChipViewHolder(View itemView) {
            super(itemView);
            chip = (Chip) itemView.findViewById(R.id.chipTextId);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////

    public void startProteinActivity() {
        intentProtein = new Intent(context, ProteinActivity.class);
        intentProtein.putExtra(Constants.ITEM_KEY, Constants.PROTEIN);
        context.startActivity(intentProtein);
    }


}
