package com.mohit.varma.apnimandi.utilites;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class Constants {

    public static final String MOBILE_COUNTRY_PREFIX = "+91";

    public static String getStringFromID(Context context,int id){
        return context.getResources().getString(id);
    }
}
