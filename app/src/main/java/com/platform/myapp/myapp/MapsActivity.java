package com.platform.myapp.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Marker mMarker;
    private Marker m_addMarker;
    public double lat, lng;
    protected android.location.LocationListener locationListener;
    protected LocationManager locationManager;
    String detailplaceName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Location_Manage();
        UpdateLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void Location_Manage(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //-- เก็บสถานะว่าได้เปิดใช้การระบุพิกัดด้วย Network กับ GPS หรือไม่
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.setTrafficEnabled(true);
                mMap.setIndoorEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                // Log.d(TAG,"onLocationChanged");
                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
                //new datacache_location("null", "null", lat, lng);
                if (mMarker != null) {
                    Log.d("MainTestMapActivity", "onCreate , check Mark");
                    mMarker.remove();
                }
                /////////////////////////////////
                try {
                    Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.isEmpty()) {
                        detailplaceName = "Waiting for Location";
                    }
                    else {
                        if (addresses.size() > 0) {
                            //detailplaceName = addresses.get(0).getFeatureName();
                            //Log.e("Json GEO Googlemap  >>>>  ",addresses.toString());
                            detailplaceName =  addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                            LatLng latLng = new LatLng(lat,lng);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("This Here :"+detailplaceName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                        }
                    }
                }
                catch(Exception e){
                    //Toast.makeText(this, "No Location Name Found", 600).show();
                    //Log.e("Detail GEO MainTestMap  >>>>>  ","No Location Name Found" );
                }


            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
    }
    ///////////////////////////////////////////////////
    //--------------------------------- เช็ค Permission แล้วให้ UpdateLocation----------------------------------------------------
    void UpdateLocation() {

        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("MapsActivity", "UpdateLocation 1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("MapsActivity", "UpdateLocation 2");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 50000, 10, locationListener);




    }


    //---------------------------------  ทำงานเมื่อปิดหรือย่อแอปพลิเคชั่นก็จะหยุดการใช้งาน Location Manager ------------------------------------------------------
    public void onResume() {
        Log.d("MapsActivity", "onResume");
        super.onResume();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isNetwork) {
            Log.d("MapsActivity", "onResume Network");
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
        }

        if (isGPS) {
            Log.d("MapsActivity", "onResume GPS");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , 50000, 10, locationListener);
            Location loc = locationManager.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
            }
        }



    }

    //---------------------------------  ทำงานเมื่อปิดหรือย่อแอปพลิเคชั่นก็จะหยุดการใช้งาน Location Manager ------------------------------------------------
    public void onPause() {
        super.onPause();
        Log.d("MapsActivity", "onPause");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Intent intentLat=new Intent(this,SosActivity.class);
        intentLat.putExtra("lat", lat);
        intentLat.putExtra("lng", lng);
        startActivity(intentLat); //หน้าส่งค่าไม่มีอะไรเปลี่ยน



        // Add a marker in Sydney and move the camera
        /*LatLng Nakhonpathom = new LatLng(13.819582, 100.045969);
        mMap.addMarker(new MarkerOptions().position(Nakhonpathom).title("Marker in Nakhonpathom"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Nakhonpathom));*/
        /*try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses.size() > 0){
                String name_locat = String.valueOf(addresses.get(0).getAddressLine(0));
                Log.e(">>>>>>>>>>>>>>>>>   ", String.valueOf(addresses.get(0).getLatitude())+"  ::  "+String.valueOf(addresses.get(0).getLongitude()));
                LatLng latLng = new LatLng(lat,lng);
                mMap.addMarker(new MarkerOptions().position(latLng).title("This Here :"+detailplaceName));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


        Double getLat () {
            return lat;
        }
        void setLat ( double lat){
            this.lat = lat;
        }

}
