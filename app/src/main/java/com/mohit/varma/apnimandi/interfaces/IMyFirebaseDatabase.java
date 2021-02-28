package com.mohit.varma.apnimandi.interfaces;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public interface IMyFirebaseDatabase {
    DatabaseReference getReference();
    StorageReference getStorageReference();
}
