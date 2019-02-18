package ru.makproductions.apocalypseweatherapp.view.show.weather;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import timber.log.Timber;

//Activity for details on phones
public class ShowWeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        initShowWeatherFragment();
        initActionBar();
    }

    private void initShowWeatherFragment() {
        ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.init(getIntent().getExtras());
        android.support.v4.app.FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.show_weather, showWeatherFragment);
        transaction.commit();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.logo_layout);
            View customView = actionBar.getCustomView();
            TextView titleView = customView.findViewById(R.id.title);
            UtilMethods.changeFontTextView(titleView);
        }
    }

}
