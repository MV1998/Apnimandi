package com.mohit.varma.apnimandi.callback;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mohit.varma.apnimandi.interfaces.NetworkChangedCallBack;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AppNetworkCallBack extends ConnectivityManager.NetworkCallback {

    private NetworkChangedCallBack networkChangedCallBack;

    /**
     * @param networkChangedCallBack
     */
    public AppNetworkCallBack(NetworkChangedCallBack networkChangedCallBack) {
        this.networkChangedCallBack = networkChangedCallBack;
    }

    /**
     * @param network
     */
    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        if(networkChangedCallBack != null){
            networkChangedCallBack.networkState(true);
        }else {
            Log.d("null", "onAvailable: ");
        }
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        if(networkChangedCallBack != null){
            networkChangedCallBack.networkState(false);
        }else {
            Log.d("null", "onAvailable: ");
        }
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
    }

    @Override
    public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
    }

    @Override
    public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
        super.onBlockedStatusChanged(network, blocked);
    }
}
