package com.mohit.varma.apnimandi.activitites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.model.UserAddress;
import com.mohit.varma.apnimandi.utilites.Constants;

import static com.mohit.varma.apnimandi.utilites.Constants.REQUEST_CODE_FOR_ADDRESS;


public class AddAddressActivity extends AppCompatActivity {
    private static final String TAG = "AddAddressActivity";
    private Toolbar AddAddressActivityToolBar;
    private TextInputEditText AddAddressActivityUserName,AddAddressActivityAddressLine1,AddAddressActivityAddressLine2,
            AddAddressActivityCityCode,AddAddressActivityPhoneNumber;
    private MaterialButton AddAddressActivityPhoneNumberAddAddressButton;
    private UserAddress userAddress = null;
    private String userName,AddressLine1,AddressLine2,CityCode,PhoneNumber;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initViews();

        if (firebaseAuth != null){
            if(firebaseAuth.getCurrentUser() != null){
                if (firebaseAuth.getCurrentUser().getPhoneNumber() != null){
                    AddAddressActivityPhoneNumber.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
                }
            }
        }

        AddAddressActivityPhoneNumberAddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = AddAddressActivityUserName.getText().toString();
                AddressLine1 = AddAddressActivityAddressLine1.getText().toString();
                AddressLine2 = AddAddressActivityAddressLine2.getText().toString();
                CityCode = AddAddressActivityCityCode.getText().toString();
                PhoneNumber = AddAddressActivityPhoneNumber.getText().toString();
                if(userName != null && !userName.isEmpty()){
                    if(AddressLine1 != null && !AddressLine1.isEmpty()){
                        if(AddressLine2 != null && !AddressLine2.isEmpty()){
                            if(CityCode != null && !CityCode.isEmpty()){
                                if(PhoneNumber != null && !PhoneNumber.isEmpty()){
                                    if(PhoneNumber.length()>=10){
                                        UserAddress userAddress = new UserAddress(userName,PhoneNumber,AddressLine1,AddressLine2,CityCode);
                                        if(userAddress != null){
                                            Intent intent=new Intent();
                                            Log.d(TAG, "onClick: " + new Gson().toJson(userAddress));
                                            intent.putExtra(Constants.USER_ADDRESS_KEY,userAddress);
                                            setResult(REQUEST_CODE_FOR_ADDRESS,intent);
                                            finish();//finishing activity
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        AddAddressActivityToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initViews(){
        AddAddressActivityUserName = findViewById(R.id.AddAddressActivityUserName);
        AddAddressActivityAddressLine1 = findViewById(R.id.AddAddressActivityAddressLine1);
        AddAddressActivityAddressLine2 = findViewById(R.id.AddAddressActivityAddressLine2);
        AddAddressActivityCityCode = findViewById(R.id.AddAddressActivityCityCode);
        AddAddressActivityPhoneNumber = findViewById(R.id.AddAddressActivityPhoneNumber);
        AddAddressActivityToolBar = findViewById(R.id.AddAddressActivityToolBar);
        AddAddressActivityPhoneNumberAddAddressButton = findViewById(R.id.AddAddressActivityPhoneNumberAddAddressButton);
        firebaseAuth = MyApplication.getFirebaseAuth();
    }
}
