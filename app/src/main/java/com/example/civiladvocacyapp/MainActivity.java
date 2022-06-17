package com.example.civiladvocacyapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rView;
    private OfficialAdapter oAdapter;
    private final ArrayList<Official> oList = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    public   static String locationZipCode = "000000";
    public   static String locationdataforrotate = "";
    public   static String locationtodisplay = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rView = findViewById(R.id.recycler);
        oAdapter = new OfficialAdapter(oList, this);
        rView.setAdapter(oAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
        oList.clear();
//        if (locationZipCode.equals("") || locationZipCode == null) {
//            ((TextView) findViewById(R.id.location)).setText("no location info");
//        } else {
//            this.doDownload();
//        }
////        Dummy Data
//        for (int i = 0; i < 10; i++) {
//            oList.add(new Official());
//        }

    }


    private void doDownload() {
        if(this.hasNetworkConnection()) {
            OfficialDownload.downloadOfficial(this, locationZipCode);
        }else{
            ((TextView) findViewById(R.id.location)).setText("No Data For Location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Data cannot be accessed/loaded without Internet connections.");
            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
    }

    private void noInternetResponse(){
        ((TextView) findViewById(R.id.location)).setText("No Data For Location");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data cannot be accessed/loaded without Internet connections.");
        builder.setTitle("No Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    @SuppressLint("MissingPermission")
    private void determineLocation() {
        if (checkAppPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some situations this can be null.
                        if (location != null) {
                            locationZipCode = getZipCode(location);
                            if (!locationdataforrotate.equals("")){
                                locationZipCode=locationdataforrotate;
                            }
                            doDownload();


                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
    private boolean checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {

                  Toast.makeText(getApplicationContext(),"Denied", Toast.LENGTH_LONG).show();
                    ((TextView) findViewById(R.id.location)).setText("No Data For Location");
                }
            }
        }
    }
    private String getZipCode(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String zipcode = addresses.get(0).getPostalCode();
            String line1 [] = addresses.get(0).getAddressLine(0).split(", ");
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s",
                    zipcode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private String getZipCodeLatLon(double lat,double lon) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            String zipcode = addresses.get(0).getPostalCode();

            sb.append(String.format(
                    Locale.getDefault(),
                    "%s",
                    zipcode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private String doLocationName(String defaultLocation) {
        Geocoder gc = new Geocoder(this);
        try {
            List<Address> address =
                    gc.getFromLocationName(defaultLocation, 1);

            if (address == null || address.isEmpty()) {
                return null;
            }
            String cnty = address.get(0).getCountryCode();

            if (cnty == null) {
                return null;
            }
            Address addressItem = address.get(0);
            String fname;
            String aarea;
            if (cnty.equals("US")) {
                fname = addressItem.getFeatureName();
                aarea = addressItem.getAdminArea();
            } else {
                fname = addressItem.getLocality();
                if (fname == null)
                    fname = addressItem.getFeatureName();
                aarea = addressItem.getCountryName();
            }
            if (fname == null || fname.isEmpty()) return null;
            if (aarea == null || aarea.isEmpty()) return null;

            double longi = address.get(0).getLongitude();
            double lati = address.get(0).getLatitude();
            String rtimeLoc=getZipCodeLatLon(lati,longi);
            return rtimeLoc;
        } catch (IOException e) {
            return null;
        }
    }
    public void locationEntered(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);

        et.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(et);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String user_input = et.getText().toString();
                if(!hasNetworkConnection()){
                    Toast.makeText(getApplicationContext(), "Functionality not available without network", Toast.LENGTH_SHORT).show();
                    noInternetResponse();
                    return;

                }
                locationZipCode=doLocationName(user_input);
                if(locationZipCode == null){
                    Toast.makeText(getApplicationContext(), "Invalid Location Entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                doDownload();
            }
        });
        builder.setTitle("Enter Address");

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.search){
//            if(!this.hasNetworkConnection()){
//                Toast.makeText(getApplicationContext(), "Functionality not available without network", Toast.LENGTH_SHORT).show();
//                return true;
//            }
            locationEntered();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
        int pos = rView.getChildLayoutPosition(view);
        Official official = oList.get(pos);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra(Official.class.getName(), official);
        startActivity(intent);
    }
    public void getLocationFromVolley(String locres) {
        if(locres == null || locres.equals("")){
            ((TextView) findViewById(R.id.location)).setText("No Data For Location");
        }
        else{
             locationtodisplay = locres;
            ((TextView) findViewById(R.id.location)).setText(locationtodisplay);
        }
    }
    public void downloadFailed(){
        Toast.makeText(this, "Error occured during downloading data from the Api", Toast.LENGTH_SHORT).show();
    }

    public void updateData(ArrayList<Official> officials) {
        oList.clear();
        oList.addAll(officials);
        oAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", locationZipCode);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
        locationdataforrotate = savedInstanceState.getString("HISTORY");
        doDownload();
    }
}