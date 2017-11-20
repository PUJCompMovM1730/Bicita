package com.pujhones.bicita.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.pujhones.bicita.R;
import com.pujhones.bicita.model.FuncionClima;

public class ClimaActivity extends AppCompatActivity {

    public static final int LOCATION_PERMISSION = 3;
    public static final String TAG = "CLIMA";
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;

    Typeface weatherFont;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    String lat;
    String lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_clima);


        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) findViewById(R.id.humidity_field);
        pressure_field = (TextView) findViewById(R.id.pressure_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously*
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION);
            }
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new
                    OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            lat = String.valueOf(location.getLatitude());
                            lng = String.valueOf(location.getLongitude());
                            Log.e(TAG, String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                            Log.i(TAG, lat + " " + lng);
                            //Hacer el execute aca

                        }
                    });
        }

        FuncionClima.placeIdTask asyncTask = new FuncionClima.placeIdTask(new FuncionClima.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humedad: " + weather_humidity);
                pressure_field.setText("Presión: " + weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });


        Log.i(TAG, "Actual pos:" + lat + " " + lng);
        asyncTask.execute(lat, lng); //  asyncTask.execute("Latitude", "Longitude")


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously*
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION);
            }
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new
                    OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            lat = String.valueOf(location.getLatitude());
                            lng = String.valueOf(location.getLongitude());
                            Log.e(TAG, String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                            Log.i(TAG, lat + " " + lng);
                            FuncionClima.placeIdTask asyncTask = new FuncionClima.placeIdTask(new FuncionClima.AsyncResponse() {
                                public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                                    cityField.setText(weather_city);
                                    updatedField.setText(weather_updatedOn);
                                    detailsField.setText(weather_description);
                                    currentTemperatureField.setText(weather_temperature);
                                    humidity_field.setText("Humedad: " + weather_humidity);
                                    pressure_field.setText("Presión: " + weather_pressure);
                                    weatherIcon.setText(Html.fromHtml(weather_iconText));

                                }
                            });


                            Log.i(TAG, "Actual pos:" + lat + " " + lng);
                            asyncTask.execute(lat, lng); //  asyncTask.execute("Latitude", "Longitude")
                        }
                    });
        }

        FuncionClima.placeIdTask asyncTask = new FuncionClima.placeIdTask(new FuncionClima.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humedad: " + weather_humidity);
                pressure_field.setText("Presión: " + weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });


        Log.i(TAG, "Actual pos:" + lat + " " + lng);
        asyncTask.execute(lat, lng); //  asyncTask.execute("Latitude", "Longitude")
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i(TAG, "ENTROOOOOOO");
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new
                            OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    lat = String.valueOf(location.getLatitude());
                                    lng = String.valueOf(location.getLongitude());
                                    Log.e(TAG, String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                                    Log.i(TAG, lat + " " + lng);
                                    FuncionClima.placeIdTask asyncTask = new FuncionClima.placeIdTask(new FuncionClima.AsyncResponse() {
                                        public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                                            cityField.setText(weather_city);
                                            updatedField.setText(weather_updatedOn);
                                            detailsField.setText(weather_description);
                                            currentTemperatureField.setText(weather_temperature);
                                            humidity_field.setText("Humedad: " + weather_humidity);
                                            pressure_field.setText("Presión: " + weather_pressure);
                                            weatherIcon.setText(Html.fromHtml(weather_iconText));

                                        }
                                    });


                                    Log.i(TAG, "Actual pos:" + lat + " " + lng);
                                    asyncTask.execute(lat, lng); //  asyncTask.execute("Latitude", "Longitude")
                                }
                            });



                // permission was granted, continue with task related to permission
                } else {
                // permission denied, disable functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
