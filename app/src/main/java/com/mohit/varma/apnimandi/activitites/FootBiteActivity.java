package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.fragments.CategoryFragment;
import com.mohit.varma.apnimandi.fragments.FrequentlyAskedQuestionFragment;
import com.mohit.varma.apnimandi.fragments.HomeFragment;
import com.mohit.varma.apnimandi.fragments.PreviousOrderFragment;
import com.mohit.varma.apnimandi.fragments.RefundTermsPolicyFragment;
import com.mohit.varma.apnimandi.interfaces.NetworkChangedCallBack;
import com.mohit.varma.apnimandi.utilites.Constants;
import com.mohit.varma.apnimandi.utilites.Dialog;
import com.mohit.varma.apnimandi.utilites.Webservice;

import java.text.DateFormat;

public class FootBiteActivity extends AppCompatActivity {

    public static NetworkChangedCallBack networkChangedCallBack;

    public static final String TAG = FootBiteActivity.class.getCanonicalName();

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View mHeaderView;
    private TextView mCustomerName;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Context context;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        set_homeFragmentatStart();
        setContentView(R.layout.activity_foot_bite);

        //initialization of views and instance variables
        context = this;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl(Webservice.FIREBASE_ROOT_REFERENCE);
        toolbar = findViewById(R.id.toolbarCustom);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        mHeaderView = navigationView.getHeaderView(0);
        mCustomerName = mHeaderView.findViewById(R.id.customer_header_name);
        //end initializing process

        if (firebaseAuth.getCurrentUser() != null) {
            if(!firebaseAuth.getCurrentUser().isAnonymous()){
                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("States").setValue("Running " + DateFormat.getDateInstance().getCalendar().getTime().toString());
                Toast.makeText(this, "Hello" + firebaseAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }
        } else {
            firebaseAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(FootBiteActivity.this,Constants.getStringFromID(context,R.string.anonymous_welcome_msg), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Dialog.createDialog(context,Constants.getStringFromID(context,R.string.anonymous_error_msg));
                }
            });
        }

        //method required to set initial toolbar state
        mToolbar_setting_for_home(toolbar);

        //method required to initiate toggle and attach it to drawer
        mSetToggleToDrawerLayout();

        //navigation drawer option item select listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Category:
                        drawerLayout.closeDrawers();
                        fragment = new CategoryFragment();
                        setFragment(fragment);
                        break;
                    case R.id.previous_order:
                        drawerLayout.closeDrawers();
                        fragment = new PreviousOrderFragment();
                        setFragment(fragment);
                        break;
                    case R.id.refund_terms_policy:
                        drawerLayout.closeDrawers();
                        fragment = new RefundTermsPolicyFragment();
                        setFragment(fragment);
                        break;
                    case R.id.frequent_asked_question:
                        drawerLayout.closeDrawers();
                        fragment = new FrequentlyAskedQuestionFragment();
                        setFragment(fragment);
                        break;
                    case R.id.logout:
                        drawerLayout.closeDrawers();
                        if(firebaseAuth.getCurrentUser() != null){
                            if (firebaseAuth.getCurrentUser().isAnonymous()) {
                                Toast.makeText(FootBiteActivity.this, "You are Currently a Anonymous User,Please Exit.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.cart:
                Intent intent = new Intent(FootBiteActivity.this, AddtoCartActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                break;
            case R.id.language:
                break;
            case R.id.about_footbite:
                break;
            case R.id.exit:
                finish();
                break;
        }
        return false;
    }

    public void mToolbar_setting_for_home(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("FootBite");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    public void mSetToggleToDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    public void setFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Container, fragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }
    }

    public void set_homeFragmentatStart() {
        Fragment fragment = null;
        fragment = new HomeFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Container, fragment, "HOME_FRAGMENT");
            fragmentTransaction.commit();
        }
    }
}