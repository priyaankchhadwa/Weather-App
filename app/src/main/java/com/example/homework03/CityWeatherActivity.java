package com.example.homework03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CityWeatherActivity extends AppCompatActivity {

    private TextView city_country_tv;
    private TextView weather_cond_tv;
    private TextView forecast_date_Tv;
    private TextView temperature_tv;

    private ImageView day_iv;
    private ImageView night_iv;

    private Button save_city_btn;
    private Button set_current_city_btn;

    private RecyclerView five_day_recycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        setTitle("City Weather");

        city_country_tv = findViewById(R.id.city_country_tv);
        weather_cond_tv = findViewById(R.id.weather_cond_tv);
        forecast_date_Tv = findViewById(R.id.forecast_date_Tv);
        temperature_tv = findViewById(R.id.temperature_tv);

        day_iv = findViewById(R.id.day_iv);
        night_iv = findViewById(R.id.night_iv);

        save_city_btn = findViewById(R.id.save_city_btn);
        set_current_city_btn = findViewById(R.id.set_current_city_btn);

        five_day_recycler = findViewById(R.id.five_day_recycler);

        Intent intent = getIntent();
        City city = (City) intent.getSerializableExtra("city");

        Log.d("asdf", city + "");
    }
}
