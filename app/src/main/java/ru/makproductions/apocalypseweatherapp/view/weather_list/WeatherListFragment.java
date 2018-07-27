package ru.makproductions.apocalypseweatherapp.view.weather_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import android.util.*;

import ru.makproductions.apocalypseweatherapp.presenter.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.WeatherResult;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

//Main fragment with list of cities and options
public class WeatherListFragment extends Fragment {
    private static final String WEATHER_MESSAGE = "weather_message";
    private static final String TOWN_NUMBER = "townNumber";
    private static final String TAG = "WeatherList########";
    private static final String PRESSURE = "PRESSURE";
    private static final String TOMMOROW_FORECAST = "TOMMOROW_FORECAST";
    private static final String WEEK_FORECAST = "WEEK_FORECAST";
    public static final String ON_PAUSE_MESSAGE = "onPauseeee!!!!!!";
    public static final String NAME = "Name: ";
    public static final String NEW_LINE = "/n";
    public static final String SPACE = " ";
    public static final String MIPMAP_TYPE = "mipmap";
    private SharedPreferences saveTown;
    private int townSelected;
    private final int SUCCESS_CODE = 666;
    private final static int VERTICAL = 1;
    private WeatherResult result;
    private boolean pressure;
    private boolean tommorowForecast;
    private boolean weekForecast;
    private WeatherListListener weatherListListener;
    private CitiesHandler citiesHandler;
    @Override
    public void onAttach(Context context) {
        weatherListListener = (WeatherListListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //RecyclerView init
        View rootView = inflater.inflate(R.layout.weather_list_fragment, container, false);
        FragmentActivity activity = getActivity();
        RecyclerView weatherRecyclerView = (RecyclerView) rootView.findViewById(R.id.cities_recycler_view);
        TextView chooseText = (TextView) rootView.findViewById(R.id.textview_choose_text);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(VERTICAL);
        weatherRecyclerView.setLayoutManager(layoutManager);
        Resources resources = getResources();
        citiesHandler = new CitiesHandler(resources);
        RVAdapter adapter = new RVAdapter(citiesHandler.getCities(),
                citiesHandler.getCitiesInEnglish());
        weatherRecyclerView.setAdapter(adapter);
        weatherRecyclerView.setHasFixedSize(true);
        //Get Prefs
        saveTown = activity.getPreferences(MODE_PRIVATE);

        //Get values from bundle or prefs
        if (savedInstanceState != null) {
            townSelected = savedInstanceState.getInt(TOWN_NUMBER);
        } else {
            townSelected = saveTown.getInt(TOWN_NUMBER, 0);
            pressure = saveTown.getBoolean(PRESSURE, false);

            tommorowForecast = saveTown.getBoolean(TOMMOROW_FORECAST, false);

            weekForecast = saveTown.getBoolean(WEEK_FORECAST, false);

        }

        //Fonts
        UtilMethods.changeFontTextView(chooseText, activity);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOWN_NUMBER, townSelected);
    }

    @Override
    public void onPause() {
        Log.d(TAG, ON_PAUSE_MESSAGE);
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

    //RecyclerView
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
            //Gets a city name in English casts to lower case and transforms to resource syntax
            String cityToShow = citiesToShow.get(position).toLowerCase();
            cityToShow = UtilMethods.formatCityName(cityToShow);
            Log.d(TAG, NAME + cityToShow);

            holder.city.setText(cityName);
            try {
                imageId = getResources().getIdentifier(cityToShow, MIPMAP_TYPE, getActivity().getPackageName());
            } catch (NullPointerException e) {
                Log.d(TAG, e.getMessage() + NEW_LINE + SPACE + NAME + cityToShow);
            }
            holder.cityImage.setImageResource(imageId);
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
            private TextView city;
            private ImageView cityImage;

            MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.city_list_item, parent, false));
                cityImage = (ImageView) itemView.findViewById(R.id.city_image_view);
                city = (TextView) itemView.findViewById(R.id.city);
                UtilMethods.changeFontTextView(city, getActivity());
                itemView.setOnClickListener(this);
                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onClick(View v) {
                townSelected = getAdapterPosition();
                showDescription();
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem showOnTop = menu.add(Menu.NONE, R.id.show_on_top_item, Menu.NONE, R.string.show_on_top);
                showOnTop.setOnMenuItemClickListener(onContextMenuClick);
            }

            private void sendToTop(int index) {
                citiesHandler.sendToTop(index);
                cities = citiesHandler.getCities();
                citiesToShow = citiesHandler.getCitiesInEnglish();
            }

            private final MenuItem.OnMenuItemClickListener onContextMenuClick = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.show_on_top_item) {
                        sendToTop(getAdapterPosition());
                        notifyDataSetChanged();
                        return true;
                    }
                    return false;
                }
            };
        }
    }

    private void showDescription() {
        result = WeatherResult.getWeatherDescription(getActivity(), townSelected, pressure, tommorowForecast, weekForecast,citiesHandler);
        weatherListListener.onListItemClick(result);
    }
}
