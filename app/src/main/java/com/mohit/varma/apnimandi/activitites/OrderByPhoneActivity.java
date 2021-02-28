package com.mohit.varma.apnimandi.activitites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mohit.varma.apnimandi.R;

public class OrderByPhoneActivity extends AppCompatActivity {
 public static final int PHONE_CALL_PERMISSION_REQUEST_CODE = 100;
 private TextView MyOrderSingleItemFirstDeliverBoyCallView, MyOrderSingleItemSecondDeliverBoyCallView;
 private Context context;
 private Toolbar OrderByPhoneActivityToolbar;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_phone);

        context = this;

        MyOrderSingleItemFirstDeliverBoyCallView = (TextView) findViewById(R.id.MyOrderSingleItemFirstDeliverBoyCallView);
        MyOrderSingleItemSecondDeliverBoyCallView = (TextView) findViewById(R.id.MyOrderSingleItemSecondDeliverBoyCallView);
        OrderByPhoneActivityToolbar = (Toolbar) findViewById(R.id.OrderByPhoneActivityToolbar);

        setSupportActionBar(OrderByPhoneActivityToolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        MyOrderSingleItemFirstDeliverBoyCallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + "8602421385"));
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else {
                    if (context != null)
                        ActivityCompat.requestPermissions(OrderByPhoneActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_PERMISSION_REQUEST_CODE);
                }
            }
        });

        MyOrderSingleItemSecondDeliverBoyCallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + "7999646778"));
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else {
                    if (context != null)
                        ActivityCompat.requestPermissions(OrderByPhoneActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_PERMISSION_REQUEST_CODE);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PHONE_CALL_PERMISSION_REQUEST_CODE == requestCode)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
    }
}
