package com.mohit.varma.apnimandi.utilites;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohit.varma.apnimandi.model.UserAddress;

public class Session{
    private static final String TAG = "Session";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(TAG, 0);
        editor = sharedPreferences.edit();
    }


    //Save Address to the sharedPreference database
    private static final String USER_ADDRESS = "USER_ADDRESS";

    public void setAddress(UserAddress userAddress) {
        if (userAddress != null) {
            Gson gson = new Gson();
            String jsonUserAddress = gson.toJson(userAddress);
            editor.putString(USER_ADDRESS, jsonUserAddress);
            editor.commit();
            editor.apply();
        }
    }

    public UserAddress getAddress() {
        String jsonUserAddress = sharedPreferences.getString(USER_ADDRESS, "");
        if (jsonUserAddress != null && !jsonUserAddress.isEmpty()) {
            UserAddress userAddress = new Gson().fromJson(jsonUserAddress, new TypeToken<UserAddress>(){}.getType());
            if (userAddress != null) {
                return userAddress;
            }
        }
        return null;
    }
}
