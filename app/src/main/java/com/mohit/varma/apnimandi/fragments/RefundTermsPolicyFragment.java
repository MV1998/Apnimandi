package com.mohit.varma.apnimandi.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohit.varma.apnimandi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RefundTermsPolicyFragment extends Fragment {


    public RefundTermsPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refund_terms_policy, container, false);
    }

}
