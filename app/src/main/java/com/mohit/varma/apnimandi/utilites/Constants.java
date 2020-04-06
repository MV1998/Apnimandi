package com.mohit.varma.apnimandi.utilites;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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

    public static final String ORDER_PLACED = "Order Placed";
    public static final String SHIPPED = "Order Shipped";
    public static final String CANCELLED = "Order Cancelled";
    public static final String DELIVERED = "Order Delivered";
    public static final String PROCESSING = "Order Processing";

    public static String getStringFromID(Context context,int id){
        return context.getResources().getString(id);
    }

    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    public static String getLocalDate() throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        return  dateOnly.format(cal.getTime());
    }
}
