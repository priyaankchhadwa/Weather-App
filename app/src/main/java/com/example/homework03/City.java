package com.example.homework03;

import java.io.Serializable;
import java.util.Date;


public class City implements Serializable {
    String country = null;
    String city = null;
    String state = null;
    Date local_observation_date_time = null;
    String weather_text = null;
    int weather_icon;
    double value;
    String unit = null;
    String key = null;
    Boolean favorite = false;

    public City() {}

    public City(String country, String city, String state, String key) {
        this.country = country;
        this.city = city;
        this.state = state;
        this.key = key;
    }

    public City(String country, String city, String state, Date local_observation_date_time, String weather_text, int weather_icon, double value, String unit, String key) {
        this.country = country;
        this.city = city;
        this.state = state;
        this.local_observation_date_time = local_observation_date_time;
        this.weather_text = weather_text;
        this.weather_icon = weather_icon;
        this.value = value;
        this.unit = unit;
        this.key = key;
    }

    public City(String country, String city, Date local_observation_date_time, String weather_text, int weather_icon, double value, String unit, String key) {
        this.country = country;
        this.city = city;
        this.local_observation_date_time = local_observation_date_time;
        this.weather_text = weather_text;
        this.weather_icon = weather_icon;
        this.value = value;
        this.unit = unit;
        this.key = key;
    }

    @Override
    public String toString() {
        return "City{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", local_observation_date_time=" + local_observation_date_time +
                ", weather_text='" + weather_text + '\'' +
                ", weather_icon=" + weather_icon +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", key='" + key + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}