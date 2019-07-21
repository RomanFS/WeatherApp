package com.example.weatherapp;

public class WeatherToImage {

    public int getImageForCode(int code) {
        if (code >= 801) {
            return R.drawable.ic_cloud;
        } else if (code == 800) {
            return R.drawable.sun_01;
        } else if (code >= 700) {
            return R.drawable.ic_cloud;
        } else if (code >= 600) {
            return R.drawable.clouds_with_snow_01;
        } else if (code >= 500) {
            return R.drawable.clouds_with_rain_01;
        } else if (code >= 300) {
            return R.drawable.sun_with_2cloud_rain_01;
        } else if (code >= 200) {
            return R.drawable.clouds_with_rain_01;
        } else return 0;
    }
}
