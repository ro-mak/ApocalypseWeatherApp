package ru.makproductions.apocalypseweatherapp.model.entity.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = RoomCity.class, parentColumns = "name", childColumns = "cityName", onDelete = CASCADE))
public class RoomCityWeather {

    @NonNull
    @PrimaryKey
    private String cityName;
    private float temperature;
    private String weatherDescription;

    public RoomCityWeather() {
    }

    public RoomCityWeather(String cityName, float temperature, String weatherDescription) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
