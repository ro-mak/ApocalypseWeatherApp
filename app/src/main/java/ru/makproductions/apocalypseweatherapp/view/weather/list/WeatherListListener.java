package ru.makproductions.apocalypseweatherapp.view.weather.list;

import ru.makproductions.apocalypseweatherapp.model.weather.repo.WeatherResult;

public interface WeatherListListener {
    void onListItemClick(WeatherResult result);
}
