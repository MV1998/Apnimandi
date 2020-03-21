package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.Item;

import java.util.ArrayList;

public class ProteinAdapter extends RecyclerView.Adapter<ProteinAdapter.ProteinViewHolder> {

    private Context context;
    private ArrayList<Item> protein_items_list;

    public ProteinAdapter(Context context, ArrayList<Item> protein_items_list) {
        this.context = context;
        this.protein_items_list = protein_items_list;
    }

    @Override
    public ProteinViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview,parent,false);
        ProteinViewHolder proteinViewHolder = new ProteinViewHolder(view);
        return proteinViewHolder;
    }

    @Override
    public void onBindViewHolder( ProteinViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return protein_items_list.size();
    }

    public class ProteinViewHolder extends RecyclerView.ViewHolder{
        public ProteinViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
