package com.mohit.varma.apnimandi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.activitites.FruitsActivity;
import com.mohit.varma.apnimandi.activitites.VegetablesActivity;

import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = CategoryFragment.class.getSimpleName();
    private FrameLayout CategoryFragmentFruitsFrameLayoutId,CategoryFragmentVegetablsFrameLayoutId;
    private Intent intentFruit,intentVegetable;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initViews(view);
        setOnClickListener();

        return view;
    }

    public void initViews(View view){
        CategoryFragmentFruitsFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentFruitsFrameLayoutId);
        CategoryFragmentVegetablsFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentVegetablsFrameLayoutId);
    }

    public void setOnClickListener(){
        CategoryFragmentFruitsFrameLayoutId.setOnClickListener(this);
        CategoryFragmentVegetablsFrameLayoutId.setOnClickListener(this);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forcategory,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeOptionMenuIDForCategory:
                Fragment fragment = null;
                fragment = new HomeFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.Container,fragment,"HOME_FRAGMENT");
                    fragmentTransaction.commit();
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.CategoryFragmentFruitsFrameLayoutId:
                startFruitsActivity();
                break;
            case R.id.CategoryFragmentVegetablsFrameLayoutId:
                startVegetablesActivity();
                break;
        }
    }

    public void startFruitsActivity(){
        intentFruit = new Intent(getActivity(), FruitsActivity.class);
        startActivity(intentFruit);
    }

    public void startVegetablesActivity(){
        intentVegetable = new Intent(getActivity(), VegetablesActivity.class);
        startActivity(intentVegetable);
    }
}
