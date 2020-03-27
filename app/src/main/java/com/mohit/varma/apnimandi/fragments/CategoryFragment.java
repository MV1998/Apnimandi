package com.mohit.varma.apnimandi.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.mohit.varma.apnimandi.activitites.BakingActivity;
import com.mohit.varma.apnimandi.activitites.FruitsActivity;
import com.mohit.varma.apnimandi.activitites.ProteinActivity;
import com.mohit.varma.apnimandi.activitites.SnacksActivity;
import com.mohit.varma.apnimandi.activitites.VegetablesActivity;
import com.mohit.varma.apnimandi.utilites.Constants;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

/**
 * A simple {@link Fragment} subclass.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = CategoryFragment.class.getSimpleName();
    private FrameLayout CategoryFragmentFruitsFrameLayoutId, CategoryFragmentVegetablsFrameLayoutId,
            CategoryFragmentSnacksFrameLayoutId, CategoryFragmentProteinFrameLayoutId, CategoryFragmentBakingFrameLayoutId;
    private Intent intentFruit, intentVegetable, intentSnacks, intentProtein, intentBacking;
    private View CategoryFragmentRootView;

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

    public void initViews(View view) {
        CategoryFragmentFruitsFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentFruitsFrameLayoutId);
        CategoryFragmentVegetablsFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentVegetablsFrameLayoutId);
        CategoryFragmentSnacksFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentSnacksFrameLayoutId);
        CategoryFragmentProteinFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentProteinFrameLayoutId);
        CategoryFragmentBakingFrameLayoutId = (FrameLayout) view.findViewById(R.id.CategoryFragmentBakingFrameLayoutId);
        CategoryFragmentRootView = (View) view.findViewById(R.id.CategoryFragmentRootView);
    }

    public void setOnClickListener() {
        CategoryFragmentFruitsFrameLayoutId.setOnClickListener(this);
        CategoryFragmentVegetablsFrameLayoutId.setOnClickListener(this);
        CategoryFragmentSnacksFrameLayoutId.setOnClickListener(this);
        CategoryFragmentProteinFrameLayoutId.setOnClickListener(this);
        CategoryFragmentBakingFrameLayoutId.setOnClickListener(this);
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
                        ShowSnackBar.snackBar(getActivity(), CategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.CategoryFragmentFruitsFrameLayoutId:
                if (getActivity() != null) {
                    if (IsInternetConnectivity.isConnected(getActivity())) {
                        startFruitsActivity();
                    } else {
                        ShowSnackBar.snackBar(getActivity(), CategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
            case R.id.CategoryFragmentVegetablsFrameLayoutId:
                if (getActivity() != null) {
                    if (IsInternetConnectivity.isConnected(getActivity())) {
                        startVegetablesActivity();
                    } else {
                        ShowSnackBar.snackBar(getActivity(), CategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
            case R.id.CategoryFragmentSnacksFrameLayoutId:
                if (getActivity() != null) {
                    if (IsInternetConnectivity.isConnected(getActivity())) {
                        startSnacksActivity();
                    } else {
                        ShowSnackBar.snackBar(getActivity(), CategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
            case R.id.CategoryFragmentProteinFrameLayoutId:
                if (getActivity() != null) {
                    if (IsInternetConnectivity.isConnected(getActivity())) {
                        startProteinActivity();
                    } else {
                        ShowSnackBar.snackBar(getActivity(), CategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
            case R.id.CategoryFragmentBakingFrameLayoutId:
                if (getActivity() != null) {
                    if (IsInternetConnectivity.isConnected(getActivity())) {
                        startBackingActivity();
                    } else {
                        ShowSnackBar.snackBar(getActivity(), CategoryFragmentRootView, getActivity().getResources().getString(R.string.please_check_internet_connectivity));
                    }
                }
                break;
        }
    }

    public void startFruitsActivity() {
        intentFruit = new Intent(getActivity(), FruitsActivity.class);
        intentFruit.putExtra(Constants.ITEM_KEY, Constants.FRUIT);
        startActivity(intentFruit);
    }

    public void startVegetablesActivity() {
        intentVegetable = new Intent(getActivity(), VegetablesActivity.class);
        intentVegetable.putExtra(Constants.ITEM_KEY, Constants.VEGETABLE);
        startActivity(intentVegetable);
    }

    public void startSnacksActivity() {
        intentSnacks = new Intent(getActivity(), SnacksActivity.class);
        intentSnacks.putExtra(Constants.ITEM_KEY, Constants.SNACKS);
        startActivity(intentSnacks);
    }

    public void startProteinActivity() {
        intentProtein = new Intent(getActivity(), ProteinActivity.class);
        intentProtein.putExtra(Constants.ITEM_KEY, Constants.PROTEIN);
        startActivity(intentProtein);
    }

    public void startBackingActivity() {
        intentBacking = new Intent(getActivity(), BakingActivity.class);
        intentBacking.putExtra(Constants.ITEM_KEY, Constants.BACKING);
        startActivity(intentBacking);
    }
}
