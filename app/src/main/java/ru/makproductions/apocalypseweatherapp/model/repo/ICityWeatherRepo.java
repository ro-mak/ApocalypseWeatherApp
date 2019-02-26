package ru.makproductions.apocalypseweatherapp.model.repo;

import java.util.Locale;

import io.reactivex.Single;
import ru.makproductions.apocalypseweatherapp.model.entity.City;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;

public interface ICityWeatherRepo {
    Single<WeatherResult> loadWeather(int townSelectedToShow, Locale locale);

    void saveCity(City city);

    void saveCityWeather(CityWeather cityWeather);

}
