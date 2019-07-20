package com.example.weatherapp.NetWorking;

import com.example.weatherapp.Home.WeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MetaWeatherAPI {
    @GET("data/2.5/weather")
    Observable<WeatherData> getLocationDetails(
            @Query("lat") Double lat, @Query("lon") Double lng,
            @Query("appid") String key, @Query("ctn") Integer days);
}
