package com.example.weatherapp.Home;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.R;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private static Integer RC_ENABLE_LOCATION = 1;
    private static Integer RC_LOCATION_PERMISSION = 2;
    private static String TAG_FORECAST_DIALOG = "forecast_dialog";
    HomeContract.Presenter mPresenter;
    LocationManager mLocationManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Location mLocation;

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mSwipeRefreshLayout.setRefreshing(true);
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

        SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        Object mLocationManager = getSystemService(Context.LOCATION_SERVICE);

        mPresenter = new HomePresenter();
        mPresenter.subscribe(this);

        initViews();

        if (checkAndAskForLocationPermissions()) {
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

        String formattedTemperatureText = String.format(getString(R.string.celcuis_temperature), "");

        temperatureTextView.setText(formattedTemperatureText); //= formattedTemperatureText
        windSpeedTextView.setText(""); //= "+ km/h";
        humidityTextView.setText(""); //= "+ %";

        //Set the weather conditions
        String weatherCode = "";

        //Set the name
        String city = "";
        String country = "";
        String region = "";
        cityNameTextView.setText(""); ;//"${city.trim()}, ${region.trim()}, ${country.trim()}"

        //Set up the forecast recycler view
        RecyclerView forecastRecyclerView = findViewById(R.id.forecast_recycler_view);
        ForecastRecyclerAdapter forecastRecyclerAdapter = ForecastRecyclerAdapter(this, weatherData.getList())
        forecastRecyclerAdapter.addActionListener {
            () ->
                    val forecastDialog = ForecastDialogFragment.getInstance(forecast)
            forecastDialog.show(supportFragmentManager, TAG_FORECAST_DIALOG)

        }
        Log.d("updateUI", "adapter set")
        forecastRecyclerView.setAdapter(forecastRecyclerAdapter);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
