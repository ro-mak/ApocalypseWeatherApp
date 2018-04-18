package ru.makproductions.apocalypseweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

import android.util.*;


public class WeatherListFragment extends Fragment {
    private static final String WEATHER_MESSAGE = "weather_message";
    private static final String TOWN_NUMBER = "townNumber";
    private static final String TAG = "WeatherList########";
    private static final String PRESSURE = "PRESSURE";
    private static final String TOMMOROW_FORECAST = "TOMMOROW_FORECAST";
    private static final String WEEK_FORECAST = "WEEK_FORECAST";
    private Button showDescriptionButton;
    private SharedPreferences saveTown;
    private int townSelected;
    private final int SUCCESS_CODE = 666;
    private final static int VERTICAL = 1;
    private String result = "";
    private boolean pressure;
    private boolean tommorowForecast;
    private boolean weekForecast;

    private WeatherListListener weatherListListener;


    @Override
    public void onAttach(Context context) {
        weatherListListener = (WeatherListListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_list_fragment, container, false);
        FragmentActivity activity = getActivity();
        RecyclerView weatherRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        TextView chooseText = (TextView) rootView.findViewById(R.id.textview_choose_text);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(VERTICAL);
        weatherRecyclerView.setLayoutManager(layoutManager);
        Resources resources = getResources();
        weatherRecyclerView.setAdapter(new RVAdapter(Arrays.asList(resources.getStringArray(R.array.cities)),
                getEnglishCitiesNamesList(resources)));
        saveTown = activity.getPreferences(MODE_PRIVATE);

        CheckBox checkBoxPressure = (CheckBox) rootView.findViewById(R.id.checkbox_pressure);
        CheckBox checkBoxTommorowForecast = (CheckBox) rootView.findViewById(R.id.checkbox_tommorow_forecast);
        CheckBox checkBoxWeekForecast = (CheckBox) rootView.findViewById(R.id.checkbox_week_forecast);
        if (savedInstanceState != null) {
            townSelected = savedInstanceState.getInt(TOWN_NUMBER);
        } else {
            townSelected = saveTown.getInt(TOWN_NUMBER, 0);
            pressure = saveTown.getBoolean(PRESSURE, false);
            checkBoxPressure.setChecked(pressure);
            tommorowForecast = saveTown.getBoolean(TOMMOROW_FORECAST, false);
            checkBoxTommorowForecast.setChecked(tommorowForecast);
            weekForecast = saveTown.getBoolean(WEEK_FORECAST, false);
            checkBoxWeekForecast.setChecked(weekForecast);
        }
        showDescriptionButton = (Button) rootView.findViewById(R.id.show_description_button);
        weatherRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        weatherRecyclerView.setHasFixedSize(true);

        checkBoxPressure.setOnClickListener(onClickListener);
        checkBoxTommorowForecast.setOnClickListener(onClickListener);
        checkBoxWeekForecast.setOnClickListener(onClickListener);
        showDescriptionButton.setOnClickListener(onClickListener);
        UtilMethods.changeFontTextView(chooseText, activity);
        UtilMethods.changeFontTextView(checkBoxPressure, activity);
        UtilMethods.changeFontTextView(checkBoxTommorowForecast, activity);
        UtilMethods.changeFontTextView(checkBoxWeekForecast, activity);
        UtilMethods.changeFontTextView(showDescriptionButton, activity);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOWN_NUMBER, townSelected);

    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPauseeee!!!!!!");
        SharedPreferences.Editor editor = saveTown.edit();
        editor.putInt(TOWN_NUMBER, townSelected);
        editor.putBoolean(PRESSURE, pressure);
        editor.putBoolean(WEEK_FORECAST, weekForecast);
        editor.putBoolean(TOMMOROW_FORECAST, tommorowForecast);
        editor.commit();
        super.onPause();

    }

    @Override
    public void onResume() {
        pressure = saveTown.getBoolean(PRESSURE, false);
        weekForecast = saveTown.getBoolean(WEEK_FORECAST, false);
        tommorowForecast = saveTown.getBoolean(TOMMOROW_FORECAST, false);
        townSelected = saveTown.getInt(TOWN_NUMBER, 0);
        super.onResume();
    }

    private List<String> getEnglishCitiesNamesList(Resources resources) {
        Configuration conf = resources.getConfiguration();
        Locale savedLocale = conf.locale;
        conf.locale = Locale.ENGLISH; // whatever you want here
        resources.updateConfiguration(conf, null); // second arg null means don't change

// retrieve resources from desired locale
        String[] str = resources.getStringArray(R.array.cities);

// restore original locale
        conf.locale = savedLocale;
        resources.updateConfiguration(conf, null);
        return Arrays.asList(str);
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {
        List<String> cities;
        List<String> citiesToShow;


        RVAdapter(List<String> cities, List<String> citiesToShow) {
            this.cities = cities;
            this.citiesToShow = citiesToShow;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyViewHolder(inflater, parent);
        }

        private int imageId;

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String cityName = cities.get(position);
            String cityToShow = citiesToShow.get(position).toLowerCase();
            cityToShow = formatCityName(cityToShow);
            Log.d(TAG,  "Name: "+cityToShow);
            holder.city.setText(cityName);
            try {
                imageId = getResources().getIdentifier(cityToShow, "mipmap", getActivity().getPackageName());
            }catch (NullPointerException e){
                Log.d(TAG,e.getMessage() + "/n Name: "+cityToShow);
            }
            holder.cityImage.setImageResource(imageId);
        }

        //So as to find an image by city name
        private String formatCityName(String name){
            char[] charArray = name.toCharArray();
            for (int i = 0; i < name.length(); i++) {
                if(charArray[i]=='-'){
                    charArray[i] = '_';
                }
            }
           return new String(charArray);
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView city;
            private ImageView cityImage;

            MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.category_list_item, parent, false));
                cityImage = (ImageView) itemView.findViewById(R.id.city_image_view);
                city = (TextView) itemView.findViewById(R.id.city);
                UtilMethods.changeFontTextView(city, getActivity());
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                townSelected = getAdapterPosition();
                result = CitiesSpec.getWeatherDescription(getActivity(), townSelected, pressure, tommorowForecast, weekForecast);
                weatherListListener.onListItemClick(result);
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.show_description_button) {
                result = CitiesSpec.getWeatherDescription(getActivity(), townSelected, pressure, tommorowForecast, weekForecast);
                weatherListListener.onListItemClick(result);
            }

            if (view.getId() == R.id.checkbox_pressure) {
                pressure = !pressure;
            } else if (view.getId() == R.id.checkbox_tommorow_forecast) {
                tommorowForecast = !tommorowForecast;
            } else if (view.getId() == R.id.checkbox_week_forecast) {
                weekForecast = !weekForecast;
            }
        }
    };
}
