package com.example.homework03;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnItemListener{

    private static City current_city;
    private ArrayList<City> city_list = new ArrayList<>();
    private ArrayList<City> city_list_from_dialog = new ArrayList<>();

    private TextView city_not_set_tv;
    private TextView no_cities_tv;
    private TextView search_and_save_tv;

    private Button set_curr_city_btn;
    private Button search_btn;

    private EditText city_et;
    private EditText country_et;

    private CardView loading_bar;

    private View curr_city_layout;
    private TextView title_lyt_tv;
    private TextView weather_cond_lyt_tv;
    private TextView temp_lyt_tv;
    private TextView updated_lyt_tv;
    private ImageView weather_icon_lyt_iv;

    private ConstraintLayout city_searchbox_view;

    private RecyclerView city_list_recycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    String LOCATION_API = "http://dataservice.accuweather.com/locations/v1/cities/%s/search?apikey=%s&q=%s";
    String GET_CURRENT_COND_API = "http://dataservice.accuweather.com/currentconditions/v1/%s?apikey=%s";
    String FORECAST_API = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/%s?apikey=%s";
    String WEATHER_ICON = "http://developer.accuweather.com/sites/default/files/%02d-s.png";


    private void GetCurrentCityDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_select_curr_city);
        dialog.setCancelable(false);

        final TextView city_in_dialog_tv = dialog.findViewById(R.id.city_et_dialog);
        final TextView country_in_dialog_tv = dialog.findViewById(R.id.country_et_dialog);

        Button cancel_in_dialog_btn = dialog.findViewById(R.id.select_curr_dialog_cancel_btn);
        Button search_in_dialog_btn = dialog.findViewById(R.id.select_curr_dialog_ok_btn);

        cancel_in_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Enter all the details to set current city", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        search_in_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city_in_dialog_tv.getText().toString().isEmpty() || country_in_dialog_tv.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter all the details to set current city", Toast.LENGTH_SHORT).show();
                } else {
                    new GetLocation(country_in_dialog_tv.getText().toString(), city_in_dialog_tv.getText().toString(), true).execute();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather App");

        city_not_set_tv = findViewById(R.id.city_not_set_tv);
        no_cities_tv = findViewById(R.id.no_cities_tv);
        search_and_save_tv = findViewById(R.id.search_and_save_tv);

        set_curr_city_btn = findViewById(R.id.set_curr_city_btn);
        search_btn = findViewById(R.id.search_btn);

        city_et = findViewById(R.id.city_et);
        country_et = findViewById(R.id.country_et);

        loading_bar = findViewById(R.id.loading_bar);
        loading_bar.setVisibility(View.INVISIBLE);

        city_searchbox_view = findViewById(R.id.city_searchbox_view);

        city_list_recycler = findViewById(R.id.city_list_recycler);
        city_list_recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        city_list_recycler.setLayoutManager(layoutManager);
        mAdapter = new CityAdapter(city_list, this);
        city_list_recycler.setAdapter(mAdapter);

        curr_city_layout = findViewById(R.id.curr_city_layout);
        title_lyt_tv = curr_city_layout.findViewById(R.id.title_lyt_tv);
        weather_cond_lyt_tv = curr_city_layout.findViewById(R.id.weather_cond_lyt_tv);
        temp_lyt_tv = curr_city_layout.findViewById(R.id.temp_lyt_tv);
        updated_lyt_tv = curr_city_layout.findViewById(R.id.updated_lyt_tv);
        weather_icon_lyt_iv = curr_city_layout.findViewById(R.id.weather_icon_lyt_iv);

        set_curr_city_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCurrentCityDialog();
            }
        });

        curr_city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCurrentCityDialog();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city_main = city_et.getText().toString();
                String country_main = country_et.getText().toString();
                if (city_main.isEmpty() || country_main.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter all the details to set current city", Toast.LENGTH_SHORT).show();
//                    hideSoftKeyboard(MainActivity.this);
                } else {
                    new GetLocation(country_main, city_main, false).execute();
                }

            }
        });
    }

    @Override
    public void onItemClick(int pos) {

    }

    class GetLocation extends AsyncTask<String, Void, String> {

        String country;
        String city;
        Boolean flag;
        String key;

        public GetLocation(String country, String city, Boolean flag) {
            this.country = country;
            this.city = city;
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection con;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

            try {
                URL url = new URL(String.format(LOCATION_API, country, getString(R.string.API_KEY), city));
                con = (HttpURLConnection) url.openConnection();
                con.connect();
                Log.d("asdf", "link: " + String.format(LOCATION_API, country, getString(R.string.API_KEY), city));
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("asdf", "in response");

                    String json = IOUtils.toString(con.getInputStream(), "UTF_8");
                    JSONArray city_list = new JSONArray(json);


                    Log.d("zxcv", "" + flag);
                    if (flag) {
                        JSONObject jobj = city_list.getJSONObject(0);
                        Log.d("asdf", "jobj: " + jobj);

                        if (jobj != null) {
                            String key = jobj.getString("Key");
                            Log.d("asdf", key);

                            this.country = jobj.getJSONObject("Country").getString("ID");
                            this.key = key;

                        } else {
                            Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                            this.key = null;
                        }
                    } else {
                        Log.d("asdf", "in else in location followed by states");
                        for (int i = 0; i < city_list.length(); i++) {
                            JSONObject jobj = city_list.getJSONObject(i);

                            String country = jobj.getJSONObject("Country").getString("ID");
                            String state = jobj.getJSONObject("AdministrativeArea").getString("ID");
                            String key = jobj.getString("Key");

                            Log.d("asdf", state);

                            city_list_from_dialog.add(new City(country, city, state, key));
                        }
                    }
                }
            } catch (
                    IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (flag) {
                if (this.key == null) {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                    loading_bar.setVisibility(View.INVISIBLE);
                } else {
                    new UpdateWeather(country, city, key).execute();
                }
            } else {
                loading_bar.setVisibility(View.INVISIBLE);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < city_list_from_dialog.size(); i++) {
                    City city = city_list_from_dialog.get(i);
                    sb.append(city.city + ", " + city.state);
                    sb.append(";");
                }
                final String[] sub_str = sb.toString().split(";");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select City")
                        .setItems(sub_str, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("asdf", "" + city_list_from_dialog.get(which));
                                City send_city = city_list_from_dialog.get(which);
                                Intent intent = new Intent(MainAC)
                            }
                        });
                builder.create().show();

            }
        }
    }

    class UpdateWeather extends AsyncTask<String, Void, Void> {
        String country;
        String city;
        String key;

        public UpdateWeather(String country, String city, String key) {
            this.country = country;
            this.city = city;
            this.key = key;
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection con;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

            try {

                URL url = new URL(String.format(GET_CURRENT_COND_API, this.key, getString(R.string.API_KEY)));

                con = (HttpURLConnection) url.openConnection();
                con.connect();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    String json = IOUtils.toString(con.getInputStream(), "UTF_8");
                    JSONArray jarray = new JSONArray(json);
                    JSONObject jobj = jarray.getJSONObject(0);

                    Date local_observation_date_time = formatter.parse(jobj.getString("LocalObservationDateTime"));
                    String weather_text = jobj.getString("WeatherText");
                    int weather_icon = jobj.getInt("WeatherIcon");
                    JSONObject tempJSON = jobj.getJSONObject("Temperature");
                    JSONObject metricJSON = tempJSON.getJSONObject("Metric");
                    double value = metricJSON.getDouble("Value");
                    String unit = metricJSON.getString("Unit");

                    MainActivity.current_city = new City(country, city, local_observation_date_time, weather_text, weather_icon, value, unit, key);
                }

            } catch (JSONException | IOException | ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            PrettyTime t = new PrettyTime();

            title_lyt_tv.setText(MainActivity.current_city.city + ", " + MainActivity.current_city.country);
            weather_cond_lyt_tv.setText(MainActivity.current_city.weather_text);
            temp_lyt_tv.setText(MainActivity.current_city.value + " °" + MainActivity.current_city.unit);
            updated_lyt_tv.setText(t.format(MainActivity.current_city.local_observation_date_time));
            Picasso.get().load(String.format(WEATHER_ICON, MainActivity.current_city.weather_icon)).into(weather_icon_lyt_iv);

            city_not_set_tv.setVisibility(View.INVISIBLE);
            set_curr_city_btn.setVisibility(View.INVISIBLE);
            loading_bar.setVisibility(View.INVISIBLE);
            curr_city_layout.setVisibility(View.VISIBLE);

            Toast.makeText(MainActivity.this, "Current city saved", Toast.LENGTH_SHORT).show();
        }
    }

    class ForecastWeather extends AsyncTask<String, Void, Void> {
        String country;
        String city;
        String key;

        public ForecastWeather(String country, String city, String key) {
            this.country = country;
            this.city = city;
            this.key = key;
        }

        @Override
        protected Void doInBackground(String... strings) {
            return null;
        }
    }
}
