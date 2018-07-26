package ru.makproductions.apocalypseweatherapp.presenter.weather_list;

import ru.makproductions.apocalypseweatherapp.model.WeatherResult;

public interface WeatherListListener {
     void onListItemClick(WeatherResult result);
}
