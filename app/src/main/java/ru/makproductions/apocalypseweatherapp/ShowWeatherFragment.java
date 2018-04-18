package ru.makproductions.apocalypseweatherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ShowWeatherFragment extends Fragment {

    private static final String APOCALYPSE_COUNTDOWN_FRAGMENT = "APOCALYPSE_COUNTDOWN_FRAGMENT";
    private static final String DATE_OF_DOOM = "DATE_OF_DOOM";

    private static final long TIME_TO_APOCALYPSE = 1546290000000L;
    private String weather_message;

    private static final String WEATHER_MESSAGE = "weather_message";

    public static ShowWeatherFragment init(Bundle bundle) {

        ShowWeatherFragment showWeatherFragment = new ShowWeatherFragment();
        if (bundle != null) {
            showWeatherFragment.setArguments(bundle);
        }
        return showWeatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_weather_fragment, container, false);
        TextView showWeatherTextView = (TextView) view.findViewById(R.id.show_weather_textview);
        Bundle args = this.getArguments();
        if (args != null) weather_message = args.getString(WEATHER_MESSAGE);
        showWeatherTextView.setText(weather_message);
        Button shareWeatherButton = (Button) view.findViewById(R.id.share_weather_button);
        shareWeatherButton.setOnClickListener(onClickListener);
        FragmentActivity activity = getActivity();
        UtilMethods.changeFontTextView(showWeatherTextView, activity);
        UtilMethods.changeFontTextView(shareWeatherButton, activity);

        createNestedFragment(this.getActivity());

        return view;
    }

    private void createNestedFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = getChildFragmentManager();
        ApocalypseCountdownFragment countdownFragment = (ApocalypseCountdownFragment) fragmentManager.findFragmentByTag(APOCALYPSE_COUNTDOWN_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putLong(DATE_OF_DOOM, TIME_TO_APOCALYPSE);
        if (countdownFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            countdownFragment = ApocalypseCountdownFragment.init(bundle);
            fragmentTransaction.replace(R.id.countdown_container, countdownFragment, APOCALYPSE_COUNTDOWN_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    public void setWeather(String weather) {
        this.weather_message = weather;
    }


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.share_weather_button) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, weather_message);
                FragmentActivity activity = getActivity();
                PackageManager packageManager = activity.getPackageManager();
                if (!packageManager.queryIntentActivities(intent, 0).isEmpty()) {
                    startActivity(intent);
                    activity.setResult(FragmentActivity.RESULT_OK);
                } else {
                    activity.setResult(FragmentActivity.RESULT_CANCELED);
                }
            }
        }
    };
}
