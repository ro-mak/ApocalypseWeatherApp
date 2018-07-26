package ru.makproductions.apocalypseweatherapp.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.makproductions.apocalypseweatherapp.view.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.network.WeatherLoader;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.model.weather_map.WeatherMap;

public class WeatherResult implements Parcelable {
    private static final String TAG = "WeatherResult";
    private static final int WEATHER_ARRAY_INDEX = 0;
    private static final String GET_WEATHER_DESCRIPTION = "getWeatherDescription: ";
    private static WeatherMap weatherMap;
    private static Gson gsonObject = new Gson();
    private String weather;
    private String pressure;
    private String tomorrowForecast;
    private List<String> weekForecast;
    private static AsyncTask<Context, Integer, JSONObject> weatherLoader;
    private WeatherResult() {
    }

   public static WeatherResult getWeatherDescription(final Context context, final int position, final boolean pressure,
                                               final boolean tommorowForecast, final boolean weekForecast,final CitiesHandler citiesHandler) {

        final WeatherResult weatherResult = new WeatherResult();
        Resources resources = context.getResources();
        final String city = citiesHandler.getCitiesInEnglish().get(position).toLowerCase();
        String cityToSearch = citiesHandler.getCitiesToFind().get(position);
        weatherLoader = new WeatherLoader(cityToSearch).execute(context);
        Log.e(TAG, GET_WEATHER_DESCRIPTION + "!!!cityToSearch: " + cityToSearch);
        UtilMethods.formatCityName(city);
        try {
            weatherMap = gsonObject.fromJson(weatherLoader.get().toString(),WeatherMap.class);
        } catch (Exception e) {
            Log.e(TAG, GET_WEATHER_DESCRIPTION + e.getMessage());
            e.printStackTrace();
        }
        updateWeatherData(context, city, position, pressure, tommorowForecast, weekForecast, weatherResult);

        return weatherResult;
    }

    private static void updateWeatherData(Context context, String city, int position, boolean pressure,
                                          boolean tommorowForecast, boolean weekForecast, WeatherResult weatherResult) {
        if (weatherMap == null) {
            Log.e(TAG, GET_WEATHER_DESCRIPTION + " WEATHERMAP NULL ");
            Log.e(TAG, "updateWeatherData: city = " + city);
        }

        Log.e(TAG, GET_WEATHER_DESCRIPTION + " city " + city);
        int weekCityId = context.getResources().getIdentifier(city + "_week_forecast", "array", context.getPackageName());


        try {
            if (weatherMap != null) {
                Log.d(TAG, GET_WEATHER_DESCRIPTION + weatherMap.toString());
                weatherResult.weather = city + " " + weatherMap.getMain().getTemp()
                        + " " + weatherMap.getWeather().get(WEATHER_ARRAY_INDEX).getDescription();
                Log.e(TAG, GET_WEATHER_DESCRIPTION + weatherResult.weather);
            }
        } catch (Exception e) {
            Log.e(TAG, GET_WEATHER_DESCRIPTION + e.getMessage());
        }
        if (pressure) {

        }
        if (tommorowForecast) {

        }
        if (weekForecast) {
            weatherResult.weekForecast = Arrays.asList(context.getResources().getStringArray(weekCityId));
        }
    }

    public String getWeather() {
        return this.weather;
    }

    public String getPressure() {
        return weatherMap.getMain().getPressure().toString();
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