package ru.makproductions.apocalypseweatherapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class WeatherResult implements Parcelable {
    public static final String TAG = "WeatherResult";
    private String weather;
    private String pressure;
    private String tomorrowForecast;
    private List<String> weekForecast;

    WeatherResult() {
    }

    static WeatherResult getWeatherDescription(Context context, int position, boolean pressure,
                                               boolean tommorowForecast, boolean weekForecast) {
        WeatherResult weatherResult = new WeatherResult();
        String city = UtilMethods.formatCityName
                (UtilMethods.getEnglishCitiesNamesList
                        (context.getResources()).get(position).toLowerCase());
        Log.e(TAG, "getWeatherDescription: city " + city);
        int weekCityId = context.getResources().getIdentifier(city + "_week_forecast", "array", context.getPackageName());
        String[] descriptions = context.getResources().getStringArray(R.array.descriptions);
        weatherResult.weather = descriptions[position];
        if (pressure) {
            weatherResult.pressure = context.getResources().getStringArray(R.array.pressure)[position];
        }
        if (tommorowForecast) {
            weatherResult.tomorrowForecast = context.getResources().getStringArray(R.array.tomorrow_forecast)[position];
        }
        if (weekForecast) {
            weatherResult.weekForecast = Arrays.asList(context.getResources().getStringArray(weekCityId));
        }
        return weatherResult;
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
