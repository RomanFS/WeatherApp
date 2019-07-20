package com.example.weatherapp.Home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.R;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private final static int RC_ENABLE_LOCATION = 1;
    private final static int RC_LOCATION_PERMISSION = 2;
    private final static String TAG_FORECAST_DIALOG = "forecast_dialog";
    HomeContract.Presenter mPresenter;
    LocationManager mLocationManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Location mLocation;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mSwipeRefreshLayout.setRefreshing(true);
            mLocation = location;

            mPresenter.refresh(mLocation.getLatitude(), mLocation.getLongitude());

            //Check if the location is not null
            //Remove the location listener as we don't need to fetch the weather again and again
            mLocation = location;
            mLocationManager.removeUpdates(this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.e("", "onCreate: ");
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mPresenter = new HomePresenter();
        mPresenter.subscribe(this);

        initViews();

        if (checkAndAskForLocationPermissions()) {
            Log.e("", "onCreate: ");
            checkGpsEnabledAndPrompt();
        }
    }

    private void initViews() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mLocation != null) {
                if (mLocation.getLatitude() != 0.0 && mLocation.getLongitude() != 0.0) {
                    mPresenter.refresh(mLocation.getLatitude(), mLocation.getLongitude());
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDataFetched(WeatherData weatherData) {
        mSwipeRefreshLayout.setRefreshing(false);
        updateUI(weatherData);
    }

    private void updateUI(WeatherData weatherData) {
        TextView temperatureTextView = findViewById(R.id.temperature_text_view);
        TextView windSpeedTextView = findViewById(R.id.wind_speed_text_view);
        TextView humidityTextView = findViewById(R.id.humidity_text_view);
        ImageView weatherImageView = findViewById(R.id.weather_image_view);
        TextView weatherConditionTextView = findViewById(R.id.weather_condition_text_view);
        TextView cityNameTextView = findViewById(R.id.city_name_text_view);

        //String formattedTemperatureText = String.format(getString(R.string.celcuis_temperature), "");

        //temperatureTextView.setText(formattedTemperatureText); //= formattedTemperatureText
        windSpeedTextView.setText(""); //= "+ km/h";
        humidityTextView.setText(""); //= "+ %";

        //Set the weather conditions
        String weatherCode = "";

        //Set the name
        String city = "";
        String country = "";
        String region = "";
        cityNameTextView.setText("");
        ;//"${city.trim()}, ${region.trim()}, ${country.trim()}"

        //Set up the forecast recycler view
        RecyclerView forecastRecyclerView = findViewById(R.id.forecast_recycler_view);
        ForecastRecyclerAdapter forecastRecyclerAdapter = new ForecastRecyclerAdapter(this, weatherData.getForecast());
       /* forecastRecyclerAdapter.addActionListener {
            () ->
                    ForecastDialogFragment
            forecastDialog = new ForecastDialogFragment.getInstance(forecast)
            forecastDialog.show(supportFragmentManager, TAG_FORECAST_DIALOG)

        }*/
        Log.d("updateUI", "adapter set");
        forecastRecyclerView.setAdapter(forecastRecyclerAdapter);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION_PERMISSION) {
            //Log.e("", "RC_LOCATION_PERMISSION: ");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("", "PERMISSION_GRANTED: ");
                checkGpsEnabledAndPrompt();
            } else {
                checkAndAskForLocationPermissions();
            }
        }
    }

    private Boolean checkAndAskForLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Log.e("", "checkAndAskForLocationPermissions: ");
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Log.e("", "checkAndAskForLocationPermissions: ");
                requestPermissions(new String[] {
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        }, RC_LOCATION_PERMISSION);
                return false;
            }
        }
        //Log.e("", "checkAndAskForLocationPermissions: ");
        return true;
    }

    private void checkGpsEnabledAndPrompt() {
        //Check if the gps is enabled
        Log.e("", "checkAndAskForLocationPermissions: ");
        boolean isLocationEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isLocationEnabled) {
            //Show alert dialog to enable gps
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("GPS is not enabled")
                    .setMessage("This app required GPS to get the weather information. Do you want to enable GPS?")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, RC_ENABLE_LOCATION);

                        dialog.dismiss();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        } else {
            requestLocationUpdates();
        }
    }

    private void requestLocationUpdates() {
        String provider = LocationManager.NETWORK_PROVIDER;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(provider, 0, 0.0f, mLocationListener);

        Location location = mLocationManager.getLastKnownLocation(provider);
        mLocationListener.onLocationChanged(location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ENABLE_LOCATION) {
            checkGpsEnabledAndPrompt();
        }
    }

    @Override
    public void onStoredDataFetched(WeatherData weatherData) {

    }

    @Override
    public void onError() {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
