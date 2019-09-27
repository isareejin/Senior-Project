package com.platform.myapp.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginFacebookActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login_facebook);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);


        List<String> permissionNeeds = Arrays.asList("email, public_profile");
        loginButton.setReadPermissions(permissionNeeds);
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent myIntent = new Intent(LoginFacebookActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
                /*
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Intent myIntent = new Intent(LoginFacebookActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id");
                request.setParameters(parameters);
                request.executeAsync();*/
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginFacebookActivity.this, "Error" + exception, Toast.LENGTH_SHORT).show();
            }
        });

/*
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

*/
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//
//
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//        AppEventsLogger.activateApp(this);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //  Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
    }


}
