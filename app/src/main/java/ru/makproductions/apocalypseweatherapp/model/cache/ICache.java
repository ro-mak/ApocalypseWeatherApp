package ru.makproductions.apocalypseweatherapp.model.cache;

import ru.makproductions.apocalypseweatherapp.model.entity.City;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;

public interface ICache {
    void saveCity(City city);

    void saveCityWeather(CityWeather cityWeather);

    City loadCity(String name);

    CityWeather loadCityWeather(String cityName);
}
