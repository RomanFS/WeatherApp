package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.Data.Forecast;

import java.io.Serializable;

public class ForecastDialogFragment extends DialogFragment {
    private static String ARGS_FORECAST;

    private ImageView mWeatherImageView;
    private TextView mHighTemperatureTextView;
    private TextView mLowTemperatureTextView;
    private TextView mTextTemperatureTextView;
    private TextView mDayTextView;
    private ImageView mCloseImageView;

    public ForecastDialogFragment() {
        ARGS_FORECAST = "args_forecast";
    }

    ForecastDialogFragment getInstance(Forecast forecast) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_FORECAST, (Serializable) forecast);

        ForecastDialogFragment fragment = new ForecastDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        super.onResume();
        if (getDialog() == null) return;
        if (getDialog().getWindow() == null) return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        assert getArguments() != null;
        Forecast forecastlist = (Forecast) getArguments().getSerializable(ARGS_FORECAST);
        assert forecastlist != null;
        //Temp forecast = forecastlist.getTemp();
        //mWeatherImageView.setImageResource(WeatherToImage.getImageForCode(forecast.code));
        //mHighTemperatureTextView.setText(forecast.getMax().toString());
        //mLowTemperatureTextView.setText(forecast.getMin().toString());
        //mTextTemperatureTextView.setText(forecast.get) = forecast.text
        //mDayTextView.setText(forecast.getDay().toString());

        mCloseImageView.setOnClickListener(v -> ForecastDialogFragment.this.dismiss()); {
        }
    }
}
