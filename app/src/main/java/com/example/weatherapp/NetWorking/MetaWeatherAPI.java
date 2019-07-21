package com.example.weatherapp.NetWorking;

import com.example.weatherapp.Data.WeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MetaWeatherAPI {
    @GET("data/2.5/forecast")
    Observable<WeatherData> getLocationDetails(
            @Query("q") String city,
            @Query("appid") String key,
            @Query("units") String units);
}
//, @Query("mode") String mode