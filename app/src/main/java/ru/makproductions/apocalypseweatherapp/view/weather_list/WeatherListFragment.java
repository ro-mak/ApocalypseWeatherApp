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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.presenter.city_search_recycler.CitySearchRecyclerAdapter;
import ru.makproductions.apocalypseweatherapp.presenter.city_search_recycler.CitySearchTextWatcher;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

import static android.content.Context.MODE_PRIVATE;

//Main fragment with list of cities and options
public class WeatherListFragment extends Fragment implements View.OnClickListener {
    private static final String WEATHER_MESSAGE = "weather_message";
    private static final String TOWN_NUMBER = "townNumber";
    private static final String TAG = "WeatherList########";
    private static final String PRESSURE = "PRESSURE";
    private static final String TOMMOROW_FORECAST = "TOMMOROW_FORECAST";
    private static final String WEEK_FORECAST = "WEEK_FORECAST";
    public static final String ON_PAUSE_MESSAGE = "onPauseeee!!!!!!";
    private SharedPreferences saveTown;
    private int townSelected;
    private final static int VERTICAL = 1;
    private boolean pressure;
    private boolean tommorowForecast;
    private boolean weekForecast;
    private WeatherListListener weatherListListener;
    private ImageButton addCityButton;
    private EditText citySearchEditText;
    private CitySearchRecyclerAdapter adapter;
    private Animation cityButtonAnimation;

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
        RecyclerView weatherRecyclerView = rootView.findViewById(R.id.cities_recycler_view);
        citySearchEditText = rootView.findViewById(R.id.edittext_choose_text);
        addCityButton = rootView.findViewById(R.id.add_city_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(VERTICAL);
        weatherRecyclerView.setLayoutManager(layoutManager);
        Resources resources = getResources();
        adapter = new CitySearchRecyclerAdapter(activity, citySearchEditText, weatherListListener);
        weatherRecyclerView.setAdapter(adapter);
        weatherRecyclerView.setHasFixedSize(false);

        cityButtonAnimation = AnimationUtils.loadAnimation(activity, R.anim.button_alpha);
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
        citySearchEditText.addTextChangedListener(new CitySearchTextWatcher(adapter));
        addCityButton.setOnClickListener(this);
        //Fonts
        UtilMethods.changeFontTextView(citySearchEditText, activity);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.add_city_button) {
            v.startAnimation(cityButtonAnimation);
        }
    }
}
