package com.mohit.varma.apnimandi.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohit.varma.apnimandi.interfaces.IMyFirebaseDatabase;

public class MyFirebaseDatabase implements IMyFirebaseDatabase {
    @Override
    public DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReferenceFromUrl("https://apnimandi-c7058.firebaseio.com/");
    }
}
