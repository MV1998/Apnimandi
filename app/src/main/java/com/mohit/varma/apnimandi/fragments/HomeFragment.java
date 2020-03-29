package com.mohit.varma.apnimandi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.adapters.HomeAdapter;
import com.mohit.varma.apnimandi.adapters.HomeInnerAdapter;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.interfaces.NetworkChangedCallBack;
import com.mohit.varma.apnimandi.model.UItem;
import com.mohit.varma.apnimandi.utilites.Constants;

import java.util.LinkedList;
import java.util.List;

import static com.mohit.varma.apnimandi.utilites.Constants.ITEMS;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements NetworkChangedCallBack {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private RecyclerView HomeFragmentRecyclerView;
    private TextView FruitsActivityNoItemAddedYetTextView;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private String category;
    private List<UItem> uItemList = new LinkedList<>();
    private HomeInnerAdapter homeInnerAdapter;
    private ProgressDialog progressDialog;
    private List<String> list;
    private View HomeFragmentRootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViewsAndInstances(view);
        showProgressDialog();
        setHasOptionsMenu(true);

        list = new LinkedList<>();
        list.add(" ");

        firebaseDatabase.child(ITEMS).child(Constants.MOST_POPULAR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    uItemList.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot Items : dataSnapshot.getChildren()) {
                            UItem uItem = Items.getValue(UItem.class);
                            uItemList.add(uItem);
                        }
                        Log.d(TAG, "onDataChange: " + new Gson().toJson(uItemList));
                        dismissProgressDialog();
                        if (uItemList != null && uItemList.size() > 0) {
                            if (homeInnerAdapter != null) {
                                dismissProgressDialog();
                                HomeFragmentRecyclerView.setVisibility(View.VISIBLE);
                                homeInnerAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                HomeFragmentRecyclerView.setVisibility(View.VISIBLE);
                                setAdapter();
                            }
                        }
                    } else {
                        HomeFragmentRecyclerView.setVisibility(View.GONE);
                        dismissProgressDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void initViewsAndInstances(View view) {
        HomeFragmentRecyclerView = (RecyclerView) view.findViewById(R.id.HomeFragmentRecyclerView);
        HomeFragmentRootView = (View) view.findViewById(R.id.HomeFragmentRootView);
        firebaseDatabase = new MyFirebaseDatabase().getReference();
        this.context = getActivity();
        progressDialog = new ProgressDialog(context);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void networkState(boolean result) {
        Log.d(TAG, "networkState: " + result);
    }

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setAdapter() {
        if (uItemList != null && uItemList.size() > 0) {
            homeInnerAdapter = new HomeInnerAdapter(getActivity(), uItemList, HomeFragmentRootView);
            HomeAdapter homeAdapter = new HomeAdapter(list, getActivity(), homeInnerAdapter);
            HomeFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            HomeFragmentRecyclerView.setHasFixedSize(true);
            HomeFragmentRecyclerView.setAdapter(homeAdapter);
        }
    }
}
