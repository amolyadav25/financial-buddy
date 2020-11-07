package com.antworksmoney.financialbuddy.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.contact.MyService;
import com.antworksmoney.financialbuddy.views.fragments.Profile.ProfileUpdateFragment;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static com.antworksmoney.financialbuddy.contact.MyService.PERMISSIONS_REQUEST_READ_CONTACTS;

public class CanNotUseAppActivity extends AppCompatActivity {
Button buttonContactPermission,buttonNotificationPermission;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_not_use_app);
        buttonContactPermission = findViewById(R.id.buttonContactPermission);
        buttonNotificationPermission = findViewById(R.id.buttonNotificationPermission);
        buttonContactPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(CanNotUseAppActivity.this, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(CanNotUseAppActivity.this, READ_PHONE_NUMBERS) !=
                                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CanNotUseAppActivity.this,
                        READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(CanNotUseAppActivity.this, new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

                } else {
Intent intent = new Intent(CanNotUseAppActivity.this,UserAuthActivity.class);
startActivity(intent);
finish();
                }
            }
        });
        buttonNotificationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isNotificationServiceRunning = isNotificationServiceRunning();
                if(!isNotificationServiceRunning){
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
            }
        });

    }
    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Mytag","Permission not granted"+PERMISSIONS_REQUEST_READ_CONTACTS);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Mytag","Permission not grantedif"+PERMISSIONS_REQUEST_READ_CONTACTS);

                    // To what you want
                    Intent intent = new Intent(CanNotUseAppActivity.this,UserAuthActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Mytag","Permission not grantedifelse"+PERMISSIONS_REQUEST_READ_CONTACTS);

                    // Bob never checked click
                    Intent intent = new Intent(this, CanNotUseAppActivity.class);
                    startActivity(intent);
                }

        }
    }
}