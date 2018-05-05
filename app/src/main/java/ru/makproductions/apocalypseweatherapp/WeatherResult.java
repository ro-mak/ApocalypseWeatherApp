package ru.makproductions.apocalypseweatherapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

class WeatherResult implements Parcelable {
    private static final String TAG = "WeatherResult";
    private String weather;
    private String pressure;
    private String tomorrowForecast;
    private List<String> weekForecast;
    private static JSONObject jsonWeather;
    private static AsyncTask<Context, Integer, JSONObject> weatherLoader;

    WeatherResult() {
    }

    static WeatherResult getWeatherDescription(final Context context, final int position, final boolean pressure,
                                               final boolean tommorowForecast, final boolean weekForecast) {

        final WeatherResult weatherResult = new WeatherResult();
        Resources resources = context.getResources();
        final String city = UtilMethods.getEnglishCitiesNamesList
                        (resources).get(position).toLowerCase();
        String cityToSearch = UtilMethods.
                transliterateFromRussianToEnglish(resources.
                        getStringArray(R.array.cities)[position]);
        weatherLoader = new WeatherLoader(cityToSearch).execute(context);
        Log.e(TAG, "getWeatherDescription: !!!cityToSearch: " + cityToSearch);
        UtilMethods.formatCityName(city);
        try {
            jsonWeather = weatherLoader.get();
        } catch (Exception e) {
            Log.e(TAG, "getWeatherDescription: " + e.getMessage());
            e.printStackTrace();
        }
        updateWeatherData(context, city, position, pressure, tommorowForecast, weekForecast, weatherResult);

        return weatherResult;
    }

    private static void updateWeatherData(Context context, String city, int position, boolean pressure,
                                          boolean tommorowForecast, boolean weekForecast, WeatherResult weatherResult) {
        if (jsonWeather == null) {
            Log.e(TAG, "getWeatherDescription: JSONWEATHER NULL ");
            Log.e(TAG, "updateWeatherData: city = " + city);
        }
        Log.e(TAG, "getWeatherDescription: city " + city);
        int weekCityId = context.getResources().getIdentifier(city + "_week_forecast", "array", context.getPackageName());
        String[] descriptions = context.getResources().getStringArray(R.array.descriptions);

        try {
            if (jsonWeather != null) {
                Log.d(TAG, "getWeatherDescription: " + jsonWeather.toString());
                weatherResult.weather = city + " " + jsonWeather.getJSONObject("main").getString("temp") + " sunny";
                Log.e(TAG, "getWeatherDescription: " + weatherResult.weather);
            }
        } catch (JSONException e) {
            Log.e(TAG, "getWeatherDescription: " + e.getMessage());
        }
        if (pressure) {
            weatherResult.pressure = context.getResources().getStringArray(R.array.pressure)[position];
        }
        if (tommorowForecast) {
            weatherResult.tomorrowForecast = context.getResources().getStringArray(R.array.tomorrow_forecast)[position];
        }
        if (weekForecast) {
            weatherResult.weekForecast = Arrays.asList(context.getResources().getStringArray(weekCityId));
        }
    }

    public String getWeather() {
        return weather;
    }

    public String getPressure() {
        return pressure;
    }

    public String getTomorrowForecast() {
        return tomorrowForecast;
    }

    public List<String> getWeekForecast() {
        return weekForecast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weather);
        dest.writeString(pressure);
        dest.writeString(tomorrowForecast);
        dest.writeList(weekForecast);

    }

    public static final Parcelable.Creator<WeatherResult> CREATOR = new Parcelable.Creator<WeatherResult>() {

        public WeatherResult createFromParcel(Parcel in) {
            return new WeatherResult(in);
        }

        public WeatherResult[] newArray(int size) {
            return new WeatherResult[size];
        }
    };

    private WeatherResult(Parcel in) {
        weather = in.readString();
        pressure = in.readString();
        tomorrowForecast = in.readString();
        weekForecast = new ArrayList<>();
        in.readList(weekForecast, null);
    }
}
