package com.mohit.varma.apnimandi.model;

public class UItemDescription {
    private String itemDescription;
    private String itemCalories;
    private String itemFat;
    private String itemProtein;

    public UItemDescription() {
    }

    public UItemDescription(String itemDescription, String itemCalories, String itemFat, String itemProtein) {
        this.itemDescription = itemDescription;
        this.itemCalories = itemCalories;
        this.itemFat = itemFat;
        this.itemProtein = itemProtein;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemCalories() {
        return itemCalories;
    }

    public void setItemCalories(String itemCalories) {
        this.itemCalories = itemCalories;
    }

    public String getItemFat() {
        return itemFat;
    }

    public void setItemFat(String itemFat) {
        this.itemFat = itemFat;
    }

    public String getItemProtein() {
        return itemProtein;
    }

    public void setItemProtein(String itemProtein) {
        this.itemProtein = itemProtein;
    }
}
