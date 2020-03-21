package com.mohit.varma.apnimandi.activitites;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.AddToCardAdapter;
import com.mohit.varma.apnimandi.database.SQLiteDataBaseConnect;
import com.mohit.varma.apnimandi.model.Item;

import java.util.List;

public class AddtoCartActivity extends AppCompatActivity {

    List<Item> items;
    private RecyclerView recyclerView;
    private AddToCardAdapter addToCardAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        toolbar = findViewById(R.id.toolbarCustom);
        mToolbar_setting_for_home(toolbar);

        items = new SQLiteDataBaseConnect(this).getitemarraylist();
        //for debugging purpose and may be remove in future
        for(Item item : items){
            Log.d("Actual->", "onCreate: "+item.getActual_price());
            Log.d("incre_decrement->", "onCreate: "+item.getIncre_decre_price());
            Log.d("final->", "onCreate: "+item.getFinal_price());
        }

        addToCardAdapter = new AddToCardAdapter(this,items);
        recyclerView = findViewById(R.id.addtocardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addToCardAdapter);
        addToCardAdapter.notifyDataSetChanged();
    }

    public void mToolbar_setting_for_home(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Cart");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }
}
