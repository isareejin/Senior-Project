package com.platform.myapp.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartMainApp extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main_app);
        FacebookSdk.sdkInitialize(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("", "StartMainApp Hello Jaaaaaaaa");
                if (isLoggedIn()) {
                    Intent myIntent = new Intent(StartMainApp.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Intent intent = new Intent(StartMainApp.this, LoginFacebookActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);

    }
}
