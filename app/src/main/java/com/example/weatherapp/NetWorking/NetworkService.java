package com.example.weatherapp.NetWorking;

import android.util.Log;

import com.example.weatherapp.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkService {
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public MetaWeatherAPI getMetaWeatherApi() {
        Log.e("", "getMetaWeatherApi: ");
        return retrofit.create(MetaWeatherAPI.class);
    }
}