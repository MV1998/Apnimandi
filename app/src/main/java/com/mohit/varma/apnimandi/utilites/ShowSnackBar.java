package com.mohit.varma.apnimandi.utilites;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.mohit.varma.apnimandi.R;

public class ShowSnackBar {
    public static void snackBar(Context context, View rootView, String message) {
        Snackbar snackbar = Snackbar.make(rootView,message, Snackbar.LENGTH_LONG);
        snackbar.setAction(context.getResources().getString(R.string.snackbar_ok_message), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
        });
        snackbar.show();
    }
}
