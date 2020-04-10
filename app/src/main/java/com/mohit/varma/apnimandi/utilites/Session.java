package com.mohit.varma.apnimandi.utilites;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UserAddress;

import java.util.List;

public class Session {
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
            UserAddress userAddress = new Gson().fromJson(jsonUserAddress, new TypeToken<UserAddress>() {
            }.getType());
            if (userAddress != null) {
                return userAddress;
            }
        }
        return null;
    }

    //save delivery charge
    public static final String DELIVERY_CHARGE = "delivery_charge";

    public void setDeliveryCharge(long deliveryCharge){
        editor.putLong(DELIVERY_CHARGE,deliveryCharge);
        editor.commit();
        editor.apply();
    }

    public long getDeliveryCharge(){
        return sharedPreferences.getLong(DELIVERY_CHARGE,0);
    }

    //save UCartList in SharePreference;
    public static final String CART_ITEM_LIST = "cart_item_list";

    public void setUCartList(List<UCart> uCartList){
        String UCartJson = new Gson().toJson(uCartList);
        editor.putString(CART_ITEM_LIST,UCartJson);
        editor.commit();
        editor.apply();
    }

    public List<UCart> getUCartList(){
        List<UCart> uCartList;
        String jsonString = sharedPreferences.getString(CART_ITEM_LIST,"");
        if(jsonString != null && !jsonString.isEmpty()){
            uCartList = new Gson().fromJson(jsonString,new TypeToken<List<UCart>>(){
            }.getType());
            if(uCartList != null && uCartList.size()>0){
                return uCartList;
            }
        }
        return null;
    }



    public static final String GRAND_TOTAL = "grand_total";

    public void setGrandTotal(long grandTotal){
        editor.putLong(GRAND_TOTAL,grandTotal);
        editor.commit();
        editor.apply();
    }

    public long getGrandTotal(){
        return sharedPreferences.getLong(GRAND_TOTAL,0);
    }
}