package com.platform.myapp.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.solver.Cache;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import static com.platform.myapp.myapp.myDBClass.TABLE_CONTACTS;

public class SosActivity extends AppCompatActivity {
    SQLiteDatabase db;
    myDBClass mDatabase;
    public double lat, lng;
    protected android.location.LocationListener locationListener;
    protected LocationManager locationManager;

    //        String uriString;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        mDatabase = new myDBClass(this);
        db = mDatabase.getReadableDatabase(); // Read Data
        Button buttonAbout = (Button) findViewById(R.id.buttonSOS);

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();


                //------------ Get Data From DataBase ------------
                Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_CONTACTS, null);
                String Email = "";
                String Tel = "";
                int count_smsSent = 0;
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            Email += cursor.getString(cursor.getColumnIndex("Email")) + ",";

                            //------------------------ Send to SMS ---------------------------
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(cursor.getString(cursor.getColumnIndex("Tel")), null, "Please Help Me!!!!!!! I'm at " + lat + " , " + lng + " (from Helper SOS Application)", null, null);
                                ++count_smsSent;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //-----------------------------


                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
                db.close();
                if (Email.length() > 0) Email = Email.substring(0, Email.length() - 1);
                 Log.e("TestShowEmail", Email + " lat " + lat);

                Toast.makeText(getApplicationContext(), "SMS Sent: " + count_smsSent + "Contacts!", Toast.LENGTH_LONG).show();


                //---------------------- Send to Email ----------------------------

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", Email, null));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, Email);
                emailIntent.putExtra(Intent.EXTRA_CC, "kanununique@gmail.com");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Please Help me!!!!!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm at " + lat + " , " + lng + " Please help me now. (from Helper SOS Application)");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
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



