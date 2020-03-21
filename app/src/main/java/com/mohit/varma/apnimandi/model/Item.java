package com.mohit.varma.apnimandi.model;

import android.graphics.Bitmap;
import android.os.Parcel;

public class Item {

    private String percentOff;
    private int imageResourceId;
    private String itemName;
    private String itemPrice;
    private Bitmap bitmap;
    private String keyindex;

    private int actual_price;
    private int incre_decre_price;
    private int final_price;

    public Item(String percentOff, int imageResourceId, String itemName, String itemPrice, int actual_price, int incre_decre_price, int final_price) {
        this.percentOff = percentOff;
        this.imageResourceId = imageResourceId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.actual_price = actual_price;
        this.incre_decre_price = incre_decre_price;
        this.final_price = final_price;
    }

    public int getActual_price() {
        return actual_price;
    }

    public int getIncre_decre_price() {
        return incre_decre_price;
    }

    public int getFinal_price() {
        return final_price;
    }

    public String getKeyindex() {
        return keyindex;
    }

    public Item(String percentOff, String itemName, String itemPrice, Bitmap bitmap, String keyindex, int actual_price, int incre_decre_price, int final_price) {
        this.percentOff = percentOff;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.bitmap = bitmap;
        this.keyindex = keyindex;
        this.actual_price = actual_price;
        this.incre_decre_price = incre_decre_price;
        this.final_price = final_price;
    }

    protected Item(Parcel in) {
        percentOff = in.readString();
        imageResourceId = in.readInt();
        itemName = in.readString();
        itemPrice = in.readString();
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPercentOff() {
        return percentOff;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }


}
