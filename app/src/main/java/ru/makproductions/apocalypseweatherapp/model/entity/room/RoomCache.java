package ru.makproductions.apocalypseweatherapp.model.entity.room;

import ru.makproductions.apocalypseweatherapp.model.cache.ICache;
import ru.makproductions.apocalypseweatherapp.model.entity.City;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.room.db.CitiesDatabase;

public class RoomCache implements ICache {
    @Override
    public void saveCity(City city) {
        RoomCity roomCity = CitiesDatabase.getInstance().getCitiesDao().findCity(city.getName());
        if (roomCity == null) {
            roomCity = new RoomCity(city.getName());
        }
        CitiesDatabase.getInstance().getCitiesDao().insert(roomCity);
    }

    @Override
    public void saveCityWeather(CityWeather cityWeather) {
        RoomCityWeather roomCityWeather = CitiesDatabase.getInstance().getWeatherDao().findForCity(cityWeather.getCityName());
        if (roomCityWeather == null) {
            roomCityWeather = new RoomCityWeather(cityWeather.getCityName(), cityWeather.getTemperature(), cityWeather.getWeatherDescription());
        }
        CitiesDatabase.getInstance().getWeatherDao().insert(roomCityWeather);
    }

    @Override
    public City loadCity(String name) {
        RoomCity roomCity = CitiesDatabase.getInstance().getCitiesDao().findCity(name);
        if (roomCity != null) {
            return new City(roomCity.getName());
        } else {
            return null;
        }
    }

    @Override
    public CityWeather loadCityWeather(String cityName) {
        RoomCityWeather roomCityWeather = CitiesDatabase.getInstance().getWeatherDao().findForCity(cityName);
        if (roomCityWeather != null) {
            return new CityWeather(roomCityWeather.getCityName(), roomCityWeather.getTemperature(), roomCityWeather.getWeatherDescription());
        } else {
            return null;
        }
    }
}
