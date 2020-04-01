package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class UItem implements Serializable{
    private int mItemId;
    private int mItemCutOffPrice;
    private int mItemPrice;
    private String mItemName;
    private String mItemImage;
    private String mItemWeight;
    private String mItemCategory;
    private boolean isPopular;
    private UItemDescription uItemDescription;

    public UItem() {

    }

    public UItem(int mItemId, int mItemCutOffPrice, int mItemPrice, String mItemName, String mItemImage, String mItemWeight, String mItemCategory,boolean isPopular,UItemDescription uItemDescription) {
        this.mItemId = mItemId;
        this.mItemCutOffPrice = mItemCutOffPrice;
        this.mItemPrice = mItemPrice;
        this.mItemName = mItemName;
        this.mItemImage = mItemImage;
        this.mItemWeight = mItemWeight;
        this.mItemCategory = mItemCategory;
        this.isPopular = isPopular;
        this.uItemDescription = uItemDescription;
    }

    public int getmItemId() {
        return mItemId;
    }

    public void setmItemId(int mItemId) {
        this.mItemId = mItemId;
    }

    public int getmItemCutOffPrice() {
        return mItemCutOffPrice;
    }

    public void setmItemCutOffPrice(int mItemCutOffPrice) {
        this.mItemCutOffPrice = mItemCutOffPrice;
    }

    public int getmItemPrice() {
        return mItemPrice;
    }

    public void setmItemPrice(int mItemPrice) {
        this.mItemPrice = mItemPrice;
    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getmItemWeight() {
        return mItemWeight;
    }

    public void setmItemWeight(String mItemWeight) {
        this.mItemWeight = mItemWeight;
    }

    public String getmItemCategory() {
        return mItemCategory;
    }

    public void setmItemCategory(String mItemCategory) {
        this.mItemCategory = mItemCategory;
    }

    public String getmItemImage() {
        return mItemImage;
    }

    public void setmItemImage(String mItemImage) {
        this.mItemImage = mItemImage;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public UItemDescription getuItemDescription() {
        return uItemDescription;
    }

    public void setuItemDescription(UItemDescription uItemDescription) {
        this.uItemDescription = uItemDescription;
    }
}