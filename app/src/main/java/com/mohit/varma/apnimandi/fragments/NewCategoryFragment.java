package com.mohit.varma.apnimandi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.ItemCategoryAdapter;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewCategoryFragment extends Fragment {

    private String[] values = {"Protein shakes", "Atta", "Salt And Sugar", "Noodles And Pasta", "Soap", "Tea And Coffee",
            "Detergent Powders", "Rice", "Cooking Oil", "Baby Products", "Ghee", "Beauty Product (Men)", "Pulses",
            "Spices", "Hair Oil", "Paste And Tooth Brush", "Home Cleaner"
            , "Beauty Product (Women)", "Dry Fruits", "Biscuits And Chocolates", "Others", "Sanitizer and Hand Washers"};

    private  View newLinearCategoryFragmentRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);

        initViews(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.itemCategories);
        setItemCategoryAdapter(recyclerView);

        return view;
    }

    private void setItemCategoryAdapter(RecyclerView recyclerView) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        List<String> strings = new ArrayList<>();
        strings.addAll(Arrays.asList(values));
        ItemCategoryAdapter itemCategoryAdapter = new ItemCategoryAdapter(strings, getContext());
        recyclerView.setAdapter(itemCategoryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forcategory, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeOptionMenuIDForCategory:
                if (getActivity() != null) {
                    if (IsInternetConnectivity.isConnected(getActivity())) {
                        Fragment fragment = null;
                        fragment = new HomeFragment();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Container, fragment, "HOME_FRAGMENT");
                            fragmentTransaction.commit();
                        }
                    } else {
                        ShowSnackBar.snackBar(getActivity(), newLinearCategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
        }
        return false;
    }

    void initViews(View view){
        newLinearCategoryFragmentRootView = (View) view.findViewById(R.id.newLinearCategoryFragmentRootView);
    }

}