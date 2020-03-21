package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mohit.varma.apnimandi.R;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.fourth};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.imageslider, null);
        ImageView imageView = view.findViewById(R.id.particularimage);
        imageView.setImageResource(images[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {
                    Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
