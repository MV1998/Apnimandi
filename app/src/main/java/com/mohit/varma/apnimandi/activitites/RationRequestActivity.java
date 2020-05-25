package com.mohit.varma.apnimandi.activitites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.Districts;
import com.mohit.varma.apnimandi.model.IndianState;
import com.mohit.varma.apnimandi.model.RUser;
import com.mohit.varma.apnimandi.model.RationRequests;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RationRequestActivity extends AppCompatActivity {
    private static final String TAG = "RationRequestActivity";
    private AutoCompleteTextView stateAutoCompleteTextView;
    private AutoCompleteTextView districtAutoCompleteTextView;
    private AutoCompleteTextView wardAutoCompleteTextView;
    private TextInputEditText userName;
    private TextInputEditText userRationCardNumber;
    private MaterialButton sendRequestButton;
    private View rootView;
    private List<IndianState> indianStates = new ArrayList<>();
    private List<String> stateList = new LinkedList<>();
    private List<String> districtList = new LinkedList<>();
    private List<Districts> districtsObjectList;
    private List<String> wardList = new LinkedList<>();
    private RUser rUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Context activity;
    private String flag = "States";
    private String state = "";
    private String district = "";
    private String ward = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ration_request);

        initViews();

        databaseReference.child(flag).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    indianStates.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot states : dataSnapshot.getChildren()) {
                            IndianState indianState = states.getValue(IndianState.class);
                            indianStates.add(indianState);
                        }
                        Log.d(TAG, "onDataChange:  " + new Gson().toJson(indianStates));
                        updateStateAutoCompleteTextView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (firebaseAuth != null){
            if (firebaseAuth.getCurrentUser() != null){
                String phoneNumber= firebaseAuth.getCurrentUser().getPhoneNumber();
                if (phoneNumber != null && !phoneNumber.isEmpty()){
                    databaseReference.child("RationRequests").orderByChild("phoneNumber").equalTo(phoneNumber).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                    for (DataSnapshot users : dataSnapshot.getChildren()) {
                                        rUser = users.getValue(RUser.class);
                                        Log.d(TAG, "rUser: " + new Gson().toJson(rUser));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

        stateAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + (String) parent.getItemAtPosition(position));
                state = (String) parent.getItemAtPosition(position);
                updateDistrictAutoCompleteTextView(state);
            }
        });

        districtAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                district = (String) parent.getItemAtPosition(position);
                updateWardAutoCompleteTextView(district);
            }
        });

        wardAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ward = (String) parent.getItemAtPosition(position);
            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                setSendRequestButton();
            }
        });
    }

    public void initViews() {
        stateAutoCompleteTextView = findViewById(R.id.stateAutoCompleteTextView);
        districtAutoCompleteTextView = findViewById(R.id.districtAutoCompleteTextView);
        wardAutoCompleteTextView = findViewById(R.id.wardAutoCompleteTextView);
        userName = findViewById(R.id.userName);
        rootView = findViewById(R.id.rootView);
        userRationCardNumber = findViewById(R.id.userRationCardNumber);
        sendRequestButton = findViewById(R.id.sendRequestButton);
        this.activity = this;
        databaseReference = new MyFirebaseDatabase().getReference();
        firebaseAuth = MyApplication.getFirebaseAuth();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    public void updateStateAutoCompleteTextView() {
        try {
            stateList.clear();
            for (int i = 0; i < indianStates.size(); i++) {
                stateList.add(indianStates.get(i).getStateName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.select_dialog_item, stateList);
            stateAutoCompleteTextView.setThreshold(1);
            stateAutoCompleteTextView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDistrictAutoCompleteTextView(String state) {
        try {
            districtList.clear();
            for (int i = 0; i < indianStates.size(); i++) {
                if (indianStates.get(i).getStateName().equalsIgnoreCase(state)) {
                    districtsObjectList = indianStates.get(i).getDistrictsList();
                }
            }
            if (districtsObjectList != null && districtsObjectList.size() > 0) {
                for (int i = 0; i < districtsObjectList.size(); i++) {
                    districtList.add(districtsObjectList.get(i).getDistrictName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, districtList);
                districtAutoCompleteTextView.setThreshold(1);
                districtAutoCompleteTextView.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateWardAutoCompleteTextView(String district) {
        try {
            wardList.clear();
            if (districtsObjectList != null && districtsObjectList.size() > 0) {
                for (int i = 0; i < districtsObjectList.size(); i++) {
                    if (districtsObjectList.get(i).getDistrictName().equalsIgnoreCase(district)) {
                        Log.d(TAG, "updateWardAutoCompleteTextView: " + new Gson().toJson(districtsObjectList.get(i).getDistrictWards()));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (this, android.R.layout.select_dialog_item, districtsObjectList.get(i).getDistrictWards());
                        wardAutoCompleteTextView.setThreshold(1);
                        wardAutoCompleteTextView.setAdapter(adapter);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSendRequestButton() {
        if (IsInternetConnectivity.isConnected(activity)) {
            if (!state.isEmpty() && !district.isEmpty() && !ward.isEmpty()) {
                String name = userName.getText().toString();
                String cardNumber = userRationCardNumber.getText().toString();
                if (name != null && !name.isEmpty()) {
                    if (cardNumber != null && !cardNumber.isEmpty()) {
                        Log.d(TAG, "setSendRequestButton: " + state + district  + ward + name + cardNumber);
                        if (firebaseAuth != null){
                            if (firebaseAuth.getCurrentUser() != null){
                                if (rUser != null){
                                    Log.d(TAG, "setSendRequestButton: " + new Gson().toJson(rUser));

                                }else{
                                    Log.d(TAG, "setSendRequestButton: " + " empty user ");
                                    RUser user = new RUser();
                                    user.setName(name);
                                    user.setRationCardNumber(cardNumber);
                                    user.setUserState(state);
                                    user.setUserDistrict(district);
                                    user.setUserWardNumber(ward);
                                    user.setPhoneNumber(firebaseAuth.getCurrentUser().getPhoneNumber());
                                    user.setUserId(firebaseAuth.getCurrentUser().getPhoneNumber());
                                    List<RationRequests> rationRequests = new LinkedList<>();
                                    RationRequests rationRequest = new RationRequests();
                                    rationRequest.setRationRequestCode(String.valueOf(new Random().nextInt(1000)));
                                    rationRequest.setRequestStatus(false);
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    rationRequest.setRequestTime(formatter.format(date));
                                    rationRequests.add(rationRequest);
                                    user.setRationRequests(rationRequests);
                                    user.setNumberOfRequest(1);
                                    if (user != null){
                                        addRUserToDatabase(user, firebaseAuth.getCurrentUser().getPhoneNumber());
                                    }
                                }
                            }
                        }
                    } else {
                        ShowSnackBar.snackBar(activity, rootView, activity.getResources().getString(R.string.enter_ration_card_number));
                    }
                } else {
                    ShowSnackBar.snackBar(activity, rootView, activity.getResources().getString(R.string.enter_user_name));
                }
            } else {
                ShowSnackBar.snackBar(activity, rootView, activity.getResources().getString(R.string.all_fields_required));
            }
        } else {
            ShowSnackBar.snackBar(activity, rootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    public void addRUserToDatabase(RUser rUser, String phoneNumber){
        databaseReference.child("RationRequests").child(phoneNumber)
                .setValue(rUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RationRequestActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }
}
