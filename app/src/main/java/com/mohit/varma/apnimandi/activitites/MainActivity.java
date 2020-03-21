package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mohit.varma.apnimandi.R;

import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "FUNCTION-INTERFACE->";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("apnimandi", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        handler = new Handler();
        firebaseAuth = FirebaseAuth.getInstance();
        splashrunner(handler, MainActivity.this);

//        if(sharedPreferences.getBoolean("firsttime",true)){
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(getApplicationContext(), ChooseLanguageActivity.class);
//                    startActivity(intent);
//                    editor.putBoolean("firsttime",false);
//                    editor.commit();
//                }
//            },2000);
//        }else {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    chooseActivity(getApplicationContext());
//                }
//            },1000);
//        }
    }

    public void splashrunner(Handler handler, final Context context) {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                chooseActivity(context);
            }
        }, getApplicationContext().getResources().getInteger(R.integer.splash_timeout));
    }

    public void chooseActivity(Context context) {
        Intent intent;
       if(firebaseAuth.getCurrentUser() != null) {
            if(firebaseAuth.getCurrentUser().isAnonymous()){
                intent = new Intent(context, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                intent = new Intent(context, FootBiteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } else {
            intent = new Intent(context, RegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}
