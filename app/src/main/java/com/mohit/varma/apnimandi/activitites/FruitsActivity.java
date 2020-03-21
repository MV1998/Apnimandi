package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.FruitsAdapter;
import com.mohit.varma.apnimandi.model.Item;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.ArrayList;

public class FruitsActivity extends AppCompatActivity {

    public static final String TAG = FruitsActivity.class.getCanonicalName();

    private ArrayList<Item> arrayList;
    private Toolbar FruitsActivityToolbar;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);

        initViewsAndInstances();

        setToolbar();

        arrayList.add(new Item("30% off",R.drawable.first,"Pateto","250g - 100",200,1,200));
        arrayList.add(new Item("40% off",R.drawable.second,"Onion","250g - 100",400,1,400));
        arrayList.add(new Item("50% off",R.drawable.third,"Coriender","250g - 100",300,1,300));
        arrayList.add(new Item("10% off",R.drawable.first,"Simple","250g - 100",500,1,500));
        arrayList.add(new Item("50% off",R.drawable.third,"Zinger","250g - 100",600,1,600));
        arrayList.add(new Item("20% off",R.drawable.first,"Alexder","250g - 100",700,1,700));
        arrayList.add(new Item("70% off",R.drawable.first,"Ateder","250g - 100",200,1,200));

        FruitsAdapter fruitsAdapter = new FruitsAdapter(arrayList,context);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fruitsAdapter);

    }

    public void initViewsAndInstances(){
        FruitsActivityToolbar = (Toolbar) findViewById(R.id.FruitsActivityToolbar);
        recyclerView = findViewById(R.id.fruitsRecyclerViewWidget);
        arrayList = new ArrayList<>();
        this.context = this;
    }

    public void setToolbar(){
        setSupportActionBar(FruitsActivityToolbar);
        FruitsActivityToolbar.setTitleTextColor(Color.parseColor(Constants.getStringFromID(context,R.color.white)));
    }

}
