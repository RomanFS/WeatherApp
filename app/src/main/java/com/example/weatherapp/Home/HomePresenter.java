package com.example.weatherapp.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.weatherapp.Constants;
import com.example.weatherapp.NetWorking.NetworkService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;

    @Override
    public void subscribe(HomeContract.View view) {
        mView = view;

        //Load data from internal storage
        WeatherData storedWeather = getFileFromStorage(mView.getContext());
        if (storedWeather != null) {
            mView.onStoredDataFetched(storedWeather);
        }
    }

    @Override
    public void unSubscribe() {
        mView = null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void refresh(Double lat, Double lon) {
        new NetworkService().getMetaWeatherApi()
                .getLocationDetails(lat, lon, Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(WeatherData weatherData) {
                        if (weatherData == null) return;
                        Log.e("", weatherData.getCity().getName());
                        mView.onDataFetched(weatherData);
                        storeFileToExternalStorage(weatherData, mView.getContext());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onNext: ", e);
                        mView.onError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void storeFileToExternalStorage(WeatherData weatherData, Context context) {
        Gson gson = new Gson();
        String weatherJson = gson.toJson(weatherData);

        try {
            if (context == null) return;
            File weatherFile = new File(context.getFilesDir(), Constants.WEATHER_FILE_NAME);
            if (weatherFile.exists()) weatherFile.delete();
            weatherFile.createNewFile();

            FileOutputStream outputStream = context.openFileOutput(Constants.WEATHER_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(weatherJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e("", "storeFileToExternalStorage: ", e);
        }
    }

    /*
     * Get the saved weather data from the file
     */
    private WeatherData getFileFromStorage(Context context) {
        try {
            URL url = getClass().getResource(Constants.WEATHER_FILE_NAME + ".txt");
            assert url != null;
            File weatherFile = new File(url.getPath());
            String weatherJson = weatherFile.list()[0].toString();
            return new Gson().fromJson(weatherJson, WeatherData.class);
        } catch (Exception e) {
            return null;
        }
    }
}
