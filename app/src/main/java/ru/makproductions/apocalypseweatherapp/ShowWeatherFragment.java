package ru.makproductions.apocalypseweatherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//Fragment for weather details
public class ShowWeatherFragment extends Fragment {

    private static final String APOCALYPSE_COUNTDOWN_FRAGMENT = "APOCALYPSE_COUNTDOWN_FRAGMENT";
    private static final String DATE_OF_DOOM = "DATE_OF_DOOM";

    private static final long TIME_TO_APOCALYPSE = 1546290000000L;
    public static final int positionOfSkyType = 2;
    public static final String TAG = "ShowWeatherFragment!!!";
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
        ImageView weatherImage = (ImageView) view.findViewById(R.id.weather_image);
        Bundle args = this.getArguments();
        if (args != null) weather_message = args.getString(WEATHER_MESSAGE);
        if (weather_message != null) {
            showWeatherTextView.setText(weather_message.replaceAll("_", " "));
            setWeatherImage(weatherImage, weather_message);
        }
        Button shareWeatherButton = (Button) view.findViewById(R.id.share_weather_button);
        shareWeatherButton.setOnClickListener(onClickListener);
        FragmentActivity activity = getActivity();
        UtilMethods.changeFontTextView(showWeatherTextView, activity);
        UtilMethods.changeFontTextView(shareWeatherButton, activity);

        createNestedFragment(this.getActivity());

        return view;
    }

    private void setWeatherImage(ImageView weatherImage, String weather_message) {
        String parsedMessage = weather_message.split(" ")[positionOfSkyType];
        //Log.d(TAG, "setWeatherImage: " + parsedMessage);
        weatherImage.setMinimumHeight(192);
        weatherImage.setMinimumWidth(192);
        if (parsedMessage.contains(getString(R.string.weather_type_sunny))) {
            weatherImage.setImageResource(R.mipmap.sunny);
        } else if (parsedMessage.contains(getString(R.string.weather_type_cloudy))) {
            weatherImage.setImageResource(R.mipmap.cloudy);
        } else if (parsedMessage.contains(getString(R.string.weather_typer_raining))) {
            weatherImage.setImageResource(R.mipmap.raining);
        } else if (parsedMessage.contains(getString(R.string.weather_type_snowing))) {
            weatherImage.setImageResource(R.mipmap.snowing);
        } else if (parsedMessage.contains(getString(R.string.weather_type_rain_with_snow))) {
            weatherImage.setImageResource(R.mipmap.rain_with_snow);
        } else if (parsedMessage.contains(getString(R.string.weather_type_rainstorm))) {
            weatherImage.setImageResource(R.mipmap.rainstorm);
        } else if (parsedMessage.contains(getString(R.string.weather_type_snowstorm))) {
            weatherImage.setImageResource(R.mipmap.snowstorm);
        }
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
