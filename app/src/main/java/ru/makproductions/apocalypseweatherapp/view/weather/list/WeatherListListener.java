package ru.makproductions.apocalypseweatherapp.view.weather.list;

import ru.makproductions.apocalypseweatherapp.model.WeatherResult;

public interface WeatherListListener {
    void onListItemClick(WeatherResult result);
}
