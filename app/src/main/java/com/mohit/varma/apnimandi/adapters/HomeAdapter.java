package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.mohit.varma.apnimandi.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.TestingHolder> {

    private List<String> list;
    private Context context;
    private HomeInnerAdapter homeInnerAdapter;
    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.


    public HomeAdapter(List<String> list, Context context, HomeInnerAdapter homeInnerAdapter) {
        this.list = list;
        this.context = context;
        this.homeInnerAdapter = homeInnerAdapter;
    }

    @Override
    public TestingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.testingpurposesingleitem, parent, false);
        TestingHolder testingHolder = new TestingHolder(view);
        return testingHolder;
    }

    @Override
    public void onBindViewHolder(TestingHolder holder, int position) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context);
        holder.viewPager.setAdapter(viewPagerAdapter);
        settimer(holder.viewPager);
        holder.textView.setText("Most Popular");
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(homeInnerAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TestingHolder extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        private TextView textView;
        private RecyclerView recyclerView;

        public TestingHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.homeViewPager);
            textView = itemView.findViewById(R.id.mostpopularTextView);
            recyclerView = itemView.findViewById(R.id.home_sub_recyclerView);
        }
    }

    public void settimer(ViewPager viewPager) {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4 - 1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
}
