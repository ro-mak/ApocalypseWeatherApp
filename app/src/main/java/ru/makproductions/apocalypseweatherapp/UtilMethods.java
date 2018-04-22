package ru.makproductions.apocalypseweatherapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UtilMethods {
    public static void changeFontTextView(TextView view, FragmentActivity activity) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/troika.otf");
        view.setTypeface(font);
    }

    //Get English names to make finding images easier
    public static List<String> getEnglishCitiesNamesList(Resources resources) {
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

    //So as to find an image by city name
    public static String formatCityName(String name) {
        char[] charArray = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (charArray[i] == '-') {
                charArray[i] = '_';
            }
        }
        return new String(charArray);
    }

    public static void setWeatherImage(Resources resources, ImageView weatherImage, String weather_message, int positionOfSkyType) {
        String parsedMessage = weather_message.split(" ")[positionOfSkyType];
        weatherImage.setMinimumHeight(192);
        weatherImage.setMinimumWidth(192);
        if (parsedMessage.contains(resources.getString(R.string.weather_type_sunny))) {
            weatherImage.setImageResource(R.mipmap.sunny);
        } else if (parsedMessage.contains(resources.getString(R.string.weather_type_cloudy))) {
            weatherImage.setImageResource(R.mipmap.cloudy);
        } else if (parsedMessage.contains(resources.getString(R.string.weather_typer_raining))) {
            weatherImage.setImageResource(R.mipmap.raining);
        } else if (parsedMessage.contains(resources.getString(R.string.weather_type_snowing))) {
            weatherImage.setImageResource(R.mipmap.snowing);
        } else if (parsedMessage.contains(resources.getString(R.string.weather_type_rain_with_snow))) {
            weatherImage.setImageResource(R.mipmap.rain_with_snow);
        } else if (parsedMessage.contains(resources.getString(R.string.weather_type_rainstorm))) {
            weatherImage.setImageResource(R.mipmap.rainstorm);
        } else if (parsedMessage.contains(resources.getString(R.string.weather_type_snowstorm))) {
            weatherImage.setImageResource(R.mipmap.snowstorm);
        }
    }
}
