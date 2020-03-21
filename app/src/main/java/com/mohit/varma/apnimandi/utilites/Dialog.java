package com.mohit.varma.apnimandi.utilites;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.mohit.varma.apnimandi.R;

public class Dialog {

    public static void createDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.exception));
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(context.getResources().getString(R.string.positive_message), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.setCancelable(true);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
