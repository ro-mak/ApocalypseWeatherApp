package ru.makproductions.apocalypseweatherapp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class WeatherResult implements Parcelable {
    public static final Parcelable.Creator<WeatherResult> CREATOR = new Parcelable.Creator<WeatherResult>() {

        public WeatherResult createFromParcel(Parcel in) {
            return new WeatherResult(in);
        }

        public WeatherResult[] newArray(int size) {
            return new WeatherResult[size];
        }
    };
    private static WeatherResult instance;
    private String weather;
    private String pressure;
    private String tomorrowForecast;
    private List<String> weekForecast;

    private WeatherResult() {
    }

    private WeatherResult(Parcel in) {
        weather = in.readString();
        pressure = in.readString();
        tomorrowForecast = in.readString();
        weekForecast = new ArrayList<>();
        in.readList(weekForecast, null);
    }

    public static WeatherResult getInstance() {
        if (instance == null) instance = new WeatherResult();
        return instance;
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

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTomorrowForecast() {
        return tomorrowForecast;
    }

    public void setTomorrowForecast(String tomorrowForecast) {
        this.tomorrowForecast = tomorrowForecast;
    }

    public List<String> getWeekForecast() {
        return weekForecast;
    }

    public void setWeekForecast(List<String> weekForecast) {
        this.weekForecast = weekForecast;
    }
}
