package com.example.homework03;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private ArrayList<City> mData;
    private OnItemListener mOnItemListener;

    public CityAdapter(ArrayList<City> mData, OnItemListener mOnItemListener) {
        this.mData = mData;
        this.mOnItemListener = mOnItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_city_list, parent, false);
        return new ViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = mData.get(position);

        PrettyTime t = new PrettyTime();

        holder.city_recycler_tv.setText(city.city + ", " + city.country);
        holder.updated_recycler_tv.setText(t.format(city.local_observation_date_time));

        holder.favorite_iv.setImageResource(android.R.drawable.star_off);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        TextView city_recycler_tv;
        TextView updated_recycler_tv;
        TextView temperature_recycler_tv;
        ImageView favorite_iv;
        OnItemListener onItemListener;
        City city;

        public ViewHolder(@NonNull final View itemView, OnItemListener onItemListener) {
            super(itemView);
            city_recycler_tv = itemView.findViewById(R.id.city_recycler_tv);
            updated_recycler_tv = itemView.findViewById(R.id.updated_recycler_tv);
            temperature_recycler_tv = itemView.findViewById(R.id.temperature_recycler_tv);
            favorite_iv = itemView.findViewById(R.id.favorite_iv);
            this.onItemListener = onItemListener;

            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onItemListener.onItemClick(getAdapterPosition());
            return false;
        }
    }

}
