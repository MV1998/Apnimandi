package com.mohit.varma.apnimandi.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohit.varma.apnimandi.interfaces.IMyFirebaseDatabase;

public class MyFirebaseDatabase implements IMyFirebaseDatabase {
    @Override
    public DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReferenceFromUrl("https://apnimandi-c7058.firebaseio.com/");
    }

    @Override
    public StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReferenceFromUrl("gs://apnimandi-c7058.appspot.com/");
    }
}
