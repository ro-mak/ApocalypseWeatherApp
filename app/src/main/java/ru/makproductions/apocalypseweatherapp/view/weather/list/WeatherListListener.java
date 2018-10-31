package ru.makproductions.apocalypseweatherapp.view.weather.list;

import ru.makproductions.apocalypseweatherapp.presenter.WeatherResult;

public interface WeatherListListener {
    void onListItemClick(WeatherResult result);
}
