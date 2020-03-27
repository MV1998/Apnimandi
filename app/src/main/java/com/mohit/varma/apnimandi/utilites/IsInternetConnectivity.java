package com.mohit.varma.apnimandi.utilites;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IsInternetConnectivity {
    /**
     * Get the network info
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
