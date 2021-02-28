package com.mohit.varma.apnimandi.activitites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.callback.AppNetworkCallBack;
import com.mohit.varma.apnimandi.interfaces.NetworkChangedCallBack;
import com.mohit.varma.apnimandi.utilites.Constants;
import com.mohit.varma.apnimandi.utilites.IsInternetConnectivity;
import com.mohit.varma.apnimandi.utilites.ShowSnackBar;
import com.mohit.varma.apnimandi.utilites.Webservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity implements NetworkChangedCallBack {
    public static final String TAG = RegistrationActivity.class.getCanonicalName();

    private TextInputEditText editText_user_mobile_number, editText_otp;
    private TextInputLayout for_number, for_otp;
    private MaterialButton send_otp_btn, app_login_btn, mSkipButton;
    private Intent intent;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ConnectivityManager connectivityManager;
    private AppNetworkCallBack appNetworkCallBack;
    private NetworkRequest networkRequestCellular;
    private MaterialButton materialButton;
    private View view;
    private ProgressDialog progressDialog;
    private InputMethodManager inputMethodManager;
    private boolean mNetworkState;
    private Context context;

    /**
     * @param savedInstanceState
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.context = this;
        editText_user_mobile_number = findViewById(R.id.editext_to_put_user_mobile_number);
        editText_otp = findViewById(R.id.editext_to_put_otp);

        send_otp_btn = findViewById(R.id.button_to_send_otp_on_mobile);
        app_login_btn = findViewById(R.id.app_login_button);
        mSkipButton = findViewById(R.id.skip_button);
        for_number = findViewById(R.id.numberID);
        for_otp = findViewById(R.id.OTP_ID);

        view = (View) findViewById(R.id.register_context);

        firebaseAuth = MyApplication.getFirebaseAuth();

        materialButton = findViewById(R.id.skip_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl(Webservice.FIREBASE_ROOT_REFERENCE);
        progressDialog = new ProgressDialog(this, R.style.CustomDialog);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (!isSmsPermissionGranted()) {
            showRequestPermissionsInfoAlertDialog();
        }

        mShowHideonScreenStarts();

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkRequestCellular = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build();
        appNetworkCallBack = new AppNetworkCallBack(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String code = credential.getSmsCode();
                Log.d(TAG, "onVerificationCompleted:" + credential);
                if (code != null) {
                    progressDialog.dismiss();
                    mShowHideonSendOTPButton();
                    Log.d("PhoneAuthActivity", "onVerificationCompleted:" + credential);
                    editText_otp.setText(credential.getSmsCode());
                    ShowSnackBar.snackBar(context, view, context.getResources().getString(R.string.we_have_sent_otp));
                } else {
                    verifyWithoutOTP(credential);
                }
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("PhoneAuthActivity", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    ShowSnackBar.snackBar(context, view, e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    ShowSnackBar.snackBar(context, view, e.getMessage());
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String s,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("PhoneAuthActivity", "onCodeSent:" + s);

                // Save verification ID and resending token so we can use them later
                mVerificationId = s;
                mResendToken = token;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.d(TAG, "onCodeAutoRetrievalTimeOut: " + s);
                ShowSnackBar.snackBar(context, view, context.getResources().getString(R.string.sms_retrival_timeout));
            }
        };


        send_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSmsPermissionGranted()) {
                    if (mNetworkState) {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        String mobile_number = editText_user_mobile_number.getText().toString();
                        if (mobile_number.isEmpty() || mobile_number.length() <= 9) {
                            Snackbar.make(view, R.string.invalid_mobile_number_msg, Snackbar.LENGTH_LONG).show();
                        } else {
                            progressDialog.setTitle(R.string.wait_msg);
                            progressDialog.setMessage(setString(R.string.trying_to_getOTP_msg));
                            progressDialog.setCancelable(false);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(Constants.MOBILE_COUNTRY_PREFIX);
                            stringBuilder.append(mobile_number);
                            sendVerificationCode(stringBuilder.toString());
                            Log.d("Phone Number", "" + stringBuilder.toString());
                            progressDialog.show();
                            new CountDownTimer(60000, 1000) {

                                @Override
                                public void onTick(long l) {
                                    progressDialog.setMessage("Resend OTP in " + l / 1000 + " Seconds");
                                }

                                @Override
                                public void onFinish() {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            }.start();
                        }
                    } else {
                        Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    showRequestPermissionsInfoAlertDialog();
                }

            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(context)) {
                    intent = new Intent(RegistrationActivity.this, FootBiteActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ShowSnackBar.snackBar(context, view, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

        app_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String code = editText_otp.getText().toString();
                if (!code.isEmpty()) {
                    progressDialog.setTitle(R.string.wait_msg);
                    progressDialog.setMessage(setString(R.string.logging_msg));
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    verifyVerificationCode(code);
                } else {
                    editText_otp.setError(setString(R.string.enter_otp_error_msg));
                }

            }
        });

    }

    /**
     * @param mPhoneNumber
     */
    public void sendVerificationCode(String mPhoneNumber) {
        PhoneAuthProvider.getInstance(firebaseAuth).verifyPhoneNumber(mPhoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);
    }

    /**
     * @param otp
     */
    public void verifyVerificationCode(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    public void verifyWithoutOTP(PhoneAuthCredential phoneAuthCredential) {
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    /**
     * @param credential
     */

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneAuthActivity", "signInWithCredential:success");
                            checkIfUserAlreadyRegisteredToApp("+91" + editText_user_mobile_number.getText().toString());
                            progressDialog.dismiss();
                            Intent intent = new Intent(RegistrationActivity.this, FootBiteActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneAuthActivity", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    /**
     * @param phone_number
     */

    public void checkIfUserAlreadyRegisteredToApp(String phone_number) {
        databaseReference.child("Users").orderByChild("phone_number").equalTo(phone_number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Snackbar.make(view, "You are welcome", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "You are new customer, you're welcome.", Snackbar.LENGTH_LONG).show();
                    addUserToDatabase(databaseReference, firebaseAuth);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param databaseReference
     * @param firebaseAuth
     */

    public void addUserToDatabase(DatabaseReference databaseReference, FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("Users").child(user.getPhoneNumber()).child("phone_number").setValue(user.getPhoneNumber());
    }

    /**
     * Component to show when login screen starts
     */

    public void mShowHideonScreenStarts() {
        editText_otp.setVisibility(View.GONE);
        for_otp.setVisibility(View.GONE);
        app_login_btn.setVisibility(View.GONE);
    }

    /**
     * Component to show and hide when send OTP button click
     */


    public void mShowHideonSendOTPButton() {
        editText_user_mobile_number.setVisibility(View.GONE);
        for_number.setVisibility(View.GONE);
        send_otp_btn.setVisibility(View.GONE);
        mSkipButton.setVisibility(View.GONE);
        editText_otp.setVisibility(View.VISIBLE);
        for_otp.setVisibility(View.VISIBLE);
        app_login_btn.setVisibility(View.VISIBLE);
    }

    private String setString(int id) {
        return getApplicationContext().getResources().getString(id);
    }

    /**
     * @param result Network Status result in boolean
     */
    @Override
    public void networkState(boolean result) {
        mNetworkState = result;
        Log.d(TAG, "networkState: " + mNetworkState + "  " + result);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        connectivityManager.registerNetworkCallback(networkRequestCellular, appNetworkCallBack);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        super.onPause();
        connectivityManager.unregisterNetworkCallback(appNetworkCallBack);
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 100);
    }


    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    /**
     * Displays an AlertDialog explaining the user why the SMS permission is going to be requests
     *
     * @param makeSystemRequest if set to true the system permission will be shown when the dialog is dismissed.
     */
    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // SMS related task you need to do.

                } else {
                    showRequestPermissionsInfoAlertDialog();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
