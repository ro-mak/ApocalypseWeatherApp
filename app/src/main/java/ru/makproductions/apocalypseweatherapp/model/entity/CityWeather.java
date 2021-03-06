package ru.makproductions.apocalypseweatherapp.model.entity;

public class CityWeather {
    private String cityName;
    private double temperature;
    private String weatherDescription;

    public CityWeather() {
    }

    public CityWeather(String cityName, double temperature, String weatherDescription) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }

    public double getTemperature() {
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
