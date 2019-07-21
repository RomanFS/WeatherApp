package com.example.weatherapp.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Data.Forecast;
import com.example.weatherapp.R;
import com.example.weatherapp.WeatherToImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Forecast> forecast;
    private ItemAttach clicker;

    interface ItemAttach {
        void onClick(Forecast forecast);
    }

    ForecastRecyclerAdapter(Context context, List<Forecast> forecast, ItemAttach clicker) {
        this.clicker = clicker;
        this.context = context;
        this.forecast = forecast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView dayTextView = itemView.findViewById(R.id.day_text_view) ;
        ImageView weatherImageView = itemView.findViewById(R.id.weather_image_view);
        TextView temperatureTextView = itemView.findViewById(R.id.temperature_text_view);
        TextView timeTextView = itemView.findViewById(R.id.time_text_view);

        @SuppressLint("SetTextI18n")
        void bindData(Integer position) {
            Forecast forecastItem = forecast.get(position);

            long time = forecastItem.getDt();
            long timestampLong = time*1000;
            Date d = new Date(timestampLong);
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat broken = new SimpleDateFormat("HH:mm");
            broken.setTimeZone(TimeZone.getDefault());

            String dayName =
                    c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

            dayTextView.setText(dayName);
            timeTextView.setText(broken.format(d));


            int tempMax = (int) Math.round(forecastItem.getMain().getTemp());
            temperatureTextView.setText(tempMax + " °C");

            weatherImageView.setImageResource(new WeatherToImage()
                    .getImageForCode(forecastItem.getWeather().get(0).getId()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clicker.onClick(forecast.get(getAdapterPosition()));
        }
    }
}
