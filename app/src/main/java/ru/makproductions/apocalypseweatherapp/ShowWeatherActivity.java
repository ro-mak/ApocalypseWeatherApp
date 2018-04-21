package ru.makproductions.apocalypseweatherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

//Activity for details on phones
public class ShowWeatherActivity extends AppCompatActivity {
    private static final String WEATHER_MESSAGE = "weather_message";
    private static final String WEATHER_BUNDLE = "weather_bundle";

    private String weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.init(getIntent().getBundleExtra(WEATHER_BUNDLE));
        android.support.v4.app.FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.show_weather, showWeatherFragment);
        transaction.commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.logo_layout);
        View customView = actionBar.getCustomView();
        TextView titleView = (TextView) customView.findViewById(R.id.title);
        UtilMethods.changeFontTextView(titleView, this);

    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.share_weather_button) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, weather);
                PackageManager packageManager = getPackageManager();
                if (!packageManager.queryIntentActivities(intent, 0).isEmpty()) {
                    startActivity(intent);
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
            }
        }
    };

}
