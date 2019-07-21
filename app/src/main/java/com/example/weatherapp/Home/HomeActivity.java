package com.example.weatherapp.Home;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.Data.Forecast;
import com.example.weatherapp.Data.WeatherData;
import com.example.weatherapp.ForecastDialogFragment;
import com.example.weatherapp.NetWorking.MetaWeatherAPI;
import com.example.weatherapp.R;
import com.example.weatherapp.WeatherToImage;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, ForecastRecyclerAdapter.ItemAttach {

    private final static String TAG_FORECAST_DIALOG = "forecast_dialog";
    HomeContract.Presenter mPresenter;
    LocationManager mLocationManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText inputCity;
    TextView cityName;


    @Override
    public void onClick(Forecast forecast) {
        ForecastDialogFragment forecastDialog = new ForecastDialogFragment(forecast);
        forecastDialog.show(getSupportFragmentManager(), TAG_FORECAST_DIALOG);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_home);

        Log.e("", "onCreate: ");
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        inputCity = findViewById(R.id.input_city);

        inputCity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.refresh(inputCity.getText().toString());
                inputCity.clearFocus();
                InputMethodManager in = (InputMethodManager)
                        this.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(inputCity.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        mPresenter = new HomePresenter();
        mPresenter.subscribe(this);

        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.refresh("");

        initViews();
    }

    private void initViews() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            String city = cityName.getText().toString();

            mPresenter.refresh(city);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onDataFetched(WeatherData weatherData) {
        mSwipeRefreshLayout.setRefreshing(false);
        updateUI(weatherData);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherData weatherData) {
        TextView temperatureTextView = findViewById(R.id.temperature_text_view);
        TextView windSpeedTextView = findViewById(R.id.wind_speed_text_view);
        TextView humidityTextView = findViewById(R.id.humidity_text_view);
        ImageView weatherImageView = findViewById(R.id.weather_image_view);
        TextView weatherConditionTextView = findViewById(R.id.weather_condition_text_view);
        cityName = findViewById(R.id.city_name);

        Forecast forecast = weatherData.getForecast().get(0);

        cityName.setText(weatherData.getCity().getName());
        weatherConditionTextView.setText(forecast.getWeather().get(0).getMain());

        Integer id = forecast.getWeather().get(0).getId();
        weatherImageView.setImageResource(new WeatherToImage().getImageForCode(id));

        int temp = (int) Math.round(forecast.getMain().getTemp());
        temperatureTextView.setText(temp + " °C");

        windSpeedTextView.setText(forecast.getWind().getSpeed().toString() + "km/h");
        humidityTextView.setText(forecast.getMain().getHumidity().toString() + "%");

        //Set up the forecast recycler view
        RecyclerView forecastRecyclerView = findViewById(R.id.forecast_recycler_view);
        ForecastRecyclerAdapter forecastRecyclerAdapter = new ForecastRecyclerAdapter(this,
                weatherData.getForecast(), this);

        Log.d("updateUI", "adapter set");
        forecastRecyclerView.setAdapter(forecastRecyclerAdapter);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onStoredDataFetched(WeatherData weatherData) {

    }

    @Override
    public void onError() {

    }


}
