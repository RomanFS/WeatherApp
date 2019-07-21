package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.Data.Forecast;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ForecastDialogFragment extends DialogFragment {

    private ImageView mWeatherImageView;
    private TextView mHighTemperatureTextView;
    private TextView mLowTemperatureTextView;
    private TextView mTextTemperatureTextView;
    private TextView mDayTextView;
    private ImageView mCloseImageView;

    private Forecast forecast;

    public ForecastDialogFragment(Forecast forecast) {
        this.forecast = forecast;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_forecast_info, container, false);

        mWeatherImageView = view.findViewById(R.id.weather_image_view);
        mHighTemperatureTextView = view.findViewById(R.id.high_temperature_text_view);
        mLowTemperatureTextView = view.findViewById(R.id.low_temperature_text_view);
        mTextTemperatureTextView = view.findViewById(R.id.weather_condition_text_view);
        mDayTextView = view.findViewById(R.id.day_text_view);
        mCloseImageView = view.findViewById(R.id.close_image_view) ;

        initViews();

        return view;
    }

    @Override
    public void onResume() {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        int tempMax = (int) Math.round(forecast.getMain().getTempMax());
        mHighTemperatureTextView.setText(tempMax + " °C");
        int tempMin = (int) Math.round(forecast.getMain().getTempMin());
        mLowTemperatureTextView.setText(tempMin + " °C");

        mWeatherImageView.setImageResource(new WeatherToImage()
                .getImageForCode(forecast.getWeather().get(0).getId()));

        long time = forecast.getDt();
        long timestampLong = time*1000;
        Date d = new Date(timestampLong);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        mDayTextView.setText(dayName);

        mTextTemperatureTextView.setText(forecast.getWeather().get(0).getMain());

        mCloseImageView.setOnClickListener(v -> ForecastDialogFragment.this.dismiss()); {
        }
    }
}
