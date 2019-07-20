package com.example.weatherapp.Home;

import android.content.Context;

import java.io.IOException;

public interface HomeContract {
    interface View {
        public void onDataFetched(WeatherData weatherData);

        public void onStoredDataFetched(WeatherData weatherData);

        public void onError();

        public Context getContext();

    }

    interface Presenter {
        public void subscribe(View view);

        public void unSubscribe();

        public void refresh(Double lat, Double lon);
    }
}
