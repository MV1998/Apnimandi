package com.mohit.varma.apnimandi;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private static FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth != null){
            return firebaseAuth;
        }else {
            return FirebaseAuth.getInstance();
        }
    }
}
