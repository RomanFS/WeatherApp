package com.example.weatherapp.Home;

import android.content.Context;

import com.example.weatherapp.Data.WeatherData;

public interface HomeContract {
    interface View {
        void onDataFetched(WeatherData weatherData);

        void onStoredDataFetched(WeatherData weatherData);

        void onError();

        Context getContext();

    }

    interface Presenter {
        void subscribe(View view);

        void unSubscribe();

        void refresh(Double lat, Double lon);
    }
}
