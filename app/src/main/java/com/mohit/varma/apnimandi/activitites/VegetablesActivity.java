package com.mohit.varma.apnimandi.activitites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.VegetablesAdapter;
import com.mohit.varma.apnimandi.model.Item;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.ArrayList;

public class VegetablesActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private VegetablesAdapter vegetablesAdapter;
    private ArrayList<Item> vegetableArrayList;
    private Toolbar VegetablesActivityToolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagetables);

        initViewsAndInstances();
        setToolbar();

        vegetableArrayList.add(new Item("30% off",R.drawable.first,"Pateto","250g - 100",200,1,200));
        vegetableArrayList.add(new Item("40% off",R.drawable.second,"Onion","250g - 100",400,1,400));
        vegetableArrayList.add(new Item("50% off",R.drawable.third,"Coriender","250g - 100",300,1,300));
        vegetableArrayList.add(new Item("10% off",R.drawable.first,"Simple","250g - 100",500,1,500));
        vegetableArrayList.add(new Item("50% off",R.drawable.third,"Zinger","250g - 100",600,1,600));
        vegetableArrayList.add(new Item("20% off",R.drawable.first,"Alexder","250g - 100",700,1,700));
        vegetableArrayList.add(new Item("70% off",R.drawable.first,"Ateder","250g - 100",200,1,200));

        vegetablesAdapter = new VegetablesAdapter(this,vegetableArrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(vegetablesAdapter);

    }

    public void initViewsAndInstances(){
        VegetablesActivityToolbar = (Toolbar) findViewById(R.id.VegetablesActivityToolbar);
        recyclerView = findViewById(R.id.vegetableRecyclerView);
        vegetableArrayList = new ArrayList<>();
        this.context = this;

    }

    public void setToolbar(){
        setSupportActionBar(VegetablesActivityToolbar);
        VegetablesActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context,R.color.white)));
    }
}
