package com.mohit.varma.apnimandi.utilites;

import android.content.Context;

public class Constants {

    public static final String MOBILE_COUNTRY_PREFIX = "+91";

    //Constant Product Category
    public static final String ITEMS = "Items";
    public static final String FRUIT = "Fruits";
    public static final String VEGETABLE = "Vegetables";
    public static final String ITEM_KEY = "item_key";
    public static final String SNACKS = "Snacks";
    public static final String PROTEIN = "Protein";
    public static final String BACKING = "Backing";
    public static final String MOST_POPULAR = "MostPopularItems";

    public static final int REQUEST_CODE_FOR_ADDRESS = 100;
    public static final String USER_ADDRESS_KEY = "user_address_key";

    public static String getStringFromID(Context context,int id){
        return context.getResources().getString(id);
    }

}
