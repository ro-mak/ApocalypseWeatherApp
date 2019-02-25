package ru.makproductions.apocalypseweatherapp.view.ui.fragments.weather.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.presenter.weather.list.WeatherListPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.view.cities.recycler.CitySearchRecyclerAdapter;
import ru.makproductions.apocalypseweatherapp.view.ui.activities.show.weather.ShowWeatherActivity;
import ru.makproductions.apocalypseweatherapp.view.ui.fragments.show.weather.ShowWeatherFragment;
import ru.makproductions.apocalypseweatherapp.view.weather.list.WeatherListView;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

//Main fragment with list of cities and options
public class WeatherListFragment extends MvpAppCompatFragment implements WeatherListView {
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TOWN_NUMBER = "townNumber";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String PRESSURE = "PRESSURE";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TOMMOROW_FORECAST = "TOMMOROW_FORECAST";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEEK_FORECAST = "WEEK_FORECAST";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String ON_PAUSE_MESSAGE = "onPauseeee!!!!!!";

    @BindView(R.id.cities_recycler_view)
    RecyclerView weatherRecyclerView;
    private int townSelected;
    private final static int VERTICAL = 1;
    private boolean pressure;
    private boolean tommorowForecast;
    private boolean weekForecast;
    private CitySearchRecyclerAdapter adapter;
    private Animation cityButtonAnimation;

    @InjectPresenter
    WeatherListPresenter presenter;

    @ProvidePresenter
    public WeatherListPresenter provideWeatherListPresenter() {
        WeatherListPresenter presenter = new WeatherListPresenter(AndroidSchedulers.mainThread());
        Timber.e("presenter created");
        return presenter;
    }

    @BindView(R.id.edittext_choose_text)
    EditText citySearchEditText;
    @BindView(R.id.add_city_button)
    ImageButton addCityButton;
    @BindView(R.id.seacrh_city_button)
    ImageButton searchCityButton;
    private SharedPreferences saveTownSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //RecyclerView init
        View rootView = inflater.inflate(R.layout.weather_list_fragment, container, false);
        ButterKnife.bind(this, rootView);
        initRecycler();
        initAnimation();
        loadPrefs(savedInstanceState);
        loadFonts();
        return rootView;
    }

    private void loadFonts() {
        UtilMethods.changeFontTextView(citySearchEditText);
    }

    private void loadPrefs(Bundle savedInstanceState) {
        saveTownSharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        if (savedInstanceState != null) {
            townSelected = savedInstanceState.getInt(TOWN_NUMBER);
        } else {
            townSelected = saveTownSharedPreferences.getInt(TOWN_NUMBER, 0);
            pressure = saveTownSharedPreferences.getBoolean(PRESSURE, false);
            tommorowForecast = saveTownSharedPreferences.getBoolean(TOMMOROW_FORECAST, false);
            weekForecast = saveTownSharedPreferences.getBoolean(WEEK_FORECAST, false);
        }
    }

    private void initAnimation() {
        cityButtonAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha);
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEATHER_MESSAGE = "weather_message";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOWN_NUMBER, townSelected);
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(VERTICAL);
        weatherRecyclerView.setLayoutManager(layoutManager);
        adapter = new CitySearchRecyclerAdapter(citySearchEditText, presenter.getCityListPresenter());
        weatherRecyclerView.setAdapter(adapter);
        weatherRecyclerView.setHasFixedSize(false);
    }

    @Override
    public void onListItemClick(WeatherResult result) {
        showWeather(result);
    }

    public void showWeather(WeatherResult result) {
        FragmentActivity activity = getActivity();
        if (activity == null) throw new NullPointerException("MainActivity null");
        View fragmentContainer = activity.findViewById(R.id.fragment_container);
        Bundle bundle = new Bundle();
        bundle.putParcelable(WEATHER_MESSAGE, result);
        //if tablet use first, if not use second
        if (fragmentContainer != null) {
            ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.init(bundle);
            android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, showWeatherFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Intent intent = new Intent(activity, ShowWeatherActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        Timber.d(ON_PAUSE_MESSAGE);
        SharedPreferences.Editor editor = saveTownSharedPreferences.edit();
        editor.putInt(TOWN_NUMBER, townSelected);
        editor.putBoolean(PRESSURE, pressure);
        editor.putBoolean(WEEK_FORECAST, weekForecast);
        editor.putBoolean(TOMMOROW_FORECAST, tommorowForecast);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onResume() {
        pressure = saveTownSharedPreferences.getBoolean(PRESSURE, false);
        weekForecast = saveTownSharedPreferences.getBoolean(WEEK_FORECAST, false);
        tommorowForecast = saveTownSharedPreferences.getBoolean(TOMMOROW_FORECAST, false);
        townSelected = saveTownSharedPreferences.getInt(TOWN_NUMBER, 0);
        super.onResume();
    }

    @OnTextChanged(R.id.edittext_choose_text)
    public void onEditTextChange(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s.toString());
    }

    @OnClick(R.id.add_city_button)
    public void onAddCityButtonClick(View v) {
        v.startAnimation(cityButtonAnimation);
    }

    @OnClick(R.id.seacrh_city_button)
    public void onSearchCityButtonClick(View v) {
        v.startAnimation(cityButtonAnimation);
    }
}
