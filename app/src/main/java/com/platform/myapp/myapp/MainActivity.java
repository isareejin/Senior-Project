package com.platform.myapp.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.platform.myapp.myapp.myDBClass.TABLE_CONTACTS;

public class MainActivity extends AppCompatActivity {
    String imageFileName;
    SQLiteDatabase db;
    myDBClass mDatabase;
    private Button loginButton;
    public double lat, lng;
    protected android.location.LocationListener locationListener;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = (Button) findViewById(R.id.login_button);
        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

        Log.e("", "MainActivity Hello");
        mDatabase = new myDBClass(this);
        db = mDatabase.getReadableDatabase(); // Read Data

        //Set View
        Button btn_SosActivity = (Button) findViewById(R.id.buttonmainSOS);
        Button btn_EmergencyCall = (Button) findViewById(R.id.buttonmainemergency);
        Button btn_Contacts = (Button) findViewById(R.id.buttonmaincontact);
        Button btn_Camera = (Button) findViewById(R.id.buttonmainphotomail);

        //Set Action
        btn_SosActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SosActivity.class);
                startActivity(intent);
            }
        });

        btn_EmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmergencyCallActivity.class);
                startActivity(intent);
            }
        });

        btn_Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                startActivity(intent);
            }
        });

        btn_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-------Open Camera----------------
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFileName = "IMG_" + timeStamp + ".jpg";
                File f = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/" + imageFileName);
                Uri uri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 0); //<---- after take photo Go to This Method
                //---------------------------
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, LoginFacebookActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getLocation();
        if (resultCode == Activity.RESULT_OK && requestCode == 0) { //if success

            Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_CONTACTS, null);
            String email = "";

            int i = 1;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Log.e("ShowMail" + i, cursor.getString(cursor.getColumnIndex("Email")));
//                        ++i;
//                        if(i!=4)
                        email += cursor.getString(cursor.getColumnIndex("Email")) + ",";
                    } while (cursor.moveToNext());
                }
            }
            if (email.length() > 0) email = email.substring(0, email.length() - 1);
            //email+=",Test@hotmail.com";

            cursor.close();
            db.close();
            String[] emailList = email.split(",");

            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/", imageFileName);
            Uri path = Uri.fromFile(filelocation);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/image");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Photo Mail Helper SOS App");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm at " + lat + " , " + lng + " Please help me now. (from Helper SOS Application)");
            emailIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));


        }

    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isNetwork) {
            Log.d("Test Location", "onResume Network");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
                }
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                    , 50000, 10, locationListener);
            Location loc = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        } else if (isGPS) {
            Log.d("MapsActivity", "onResume GPS");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , 50000, 10, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }

    }



}
