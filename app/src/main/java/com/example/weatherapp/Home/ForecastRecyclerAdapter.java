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

import java.util.List;

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.ViewHolder> {
    Context context;
    List<Forecast> forecast;

    //private Object mListener = (ForecastList forecast) -> {};

    public ForecastRecyclerAdapter(Context context, List<Forecast> forecast) {
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
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        TextView dayTextView = itemView.findViewById(R.id.day_text_view) ;
        ImageView weatherImageView = itemView.findViewById(R.id.weather_image_view);
        TextView temperatureTextView = itemView.findViewById(R.id.temperature_text_view);

        @SuppressLint("SetTextI18n")
        void bindData(Integer position) {
            Forecast forecastItem = forecast.get(position);

            dayTextView.setText(forecastItem.getMain().getTemp().toString());
            //String high = forecastItem.getTemp().getMax().toString();
            //val low = forecast.low.toInt();
            //val formattedTemperatureText = String.format(context.getString(R.string.celcuis_temperature), ((high + low) / 2).toString())
            //temperatureTextView.text = formattedTemperatureText

            //weatherImageView.setImageResource(WeatherToImage.getImageForCode(forecast?.code
             //       ?: "3200"))
        }

        @Override
        public void onClick(View v) {
            Log.d("Recycle", "onClick: ");
            //mListener(forecastList.get(getAdapterPosition()));
        }
    }

    void addActionListener(Object listener) {
        //mListener = listener;
    }
}
