package com.mohit.varma.apnimandi.serializables;

import com.mohit.varma.apnimandi.model.UCart;
import com.mohit.varma.apnimandi.model.UItem;

import java.io.Serializable;

public class SerializableUCart implements Serializable {
    private UItem uItem;

    public SerializableUCart(){

    }

    public SerializableUCart(UItem uItem) {
        this.uItem = uItem;
    }

    public UItem getuItem() {
        return uItem;
    }

    public void setuItem(UCart uCart) {
        this.uItem = uItem;
    }
}
