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
    private String weatherDescription;
    private double pressure;
    private double temperature;
    private String cityName;
    private String tomorrowForecast;
    private List<String> weekForecast;

    private WeatherResult(Parcel in) {
        cityName = in.readString();
        weatherDescription = in.readString();
        temperature = in.readDouble();
        pressure = in.readDouble();
        tomorrowForecast = in.readString();
        weekForecast = new ArrayList<>();
        in.readList(weekForecast, null);
    }

    private WeatherResult() {
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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
        dest.writeString(cityName);
        dest.writeString(weatherDescription);
        dest.writeDouble(temperature);
        dest.writeDouble(pressure);
        dest.writeString(tomorrowForecast);
        dest.writeList(weekForecast);

    }

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
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
