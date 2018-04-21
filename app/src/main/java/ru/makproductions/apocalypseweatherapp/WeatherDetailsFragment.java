package ru.makproductions.apocalypseweatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class WeatherDetailsFragment extends Fragment {

    public static final boolean HAS_FIXED_SIZE_TRUE = true;
    private static final String WEATHER_MESSAGE = "weather_message";
    private WeatherResult weatherResult;

    public static WeatherDetailsFragment init(Bundle bundle) {
        WeatherDetailsFragment weatherDetailsFragment = new WeatherDetailsFragment();
        if (bundle != null) {
            weatherDetailsFragment.setArguments(bundle);
        }
        return weatherDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_details_fragment, container, false);
        RecyclerView forecastRecyclerView = (RecyclerView) rootView.findViewById(R.id.forecast_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        forecastRecyclerView.setLayoutManager(layoutManager);
        forecastRecyclerView.setHasFixedSize(HAS_FIXED_SIZE_TRUE);
        Bundle args = getArguments();
        if (args != null) {
            weatherResult = args.getParcelable(WEATHER_MESSAGE);
        }
        if(weatherResult!=null) {
            List<String> weekForecast = weatherResult.getWeekForecast();
            if (weekForecast != null) {
                forecastRecyclerView.setAdapter(new ForecastRecyclerViewAdapter(weekForecast));
            }
        }else{
            throw new NullPointerException("WeatherDetailsFragment!!! weatherResult is null");
        }
        return rootView;
    }

    private class ForecastRecyclerViewAdapter
            extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {
        private List<String> forecastList;

        public ForecastRecyclerViewAdapter(List<String> forecastList) {
            this.forecastList = forecastList;
        }

        @Override
        public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ForecastViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ForecastViewHolder holder, int position) {
            holder.forecastTextView.setText(forecastList.get(position));
        }

        @Override
        public int getItemCount() {
            return forecastList.size();
        }

        class ForecastViewHolder extends RecyclerView.ViewHolder {
            private TextView forecastTextView;
            private ImageView forecastImageView;

            public ForecastViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.forecast_list_item, parent, false));
                forecastTextView = itemView.findViewById(R.id.forecast_textview);
                UtilMethods.changeFontTextView(forecastTextView,getActivity());
                forecastImageView = itemView.findViewById(R.id.forecast_image_view);
            }
        }
    }
}
