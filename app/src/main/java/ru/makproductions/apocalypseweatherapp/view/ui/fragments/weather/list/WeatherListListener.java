package ru.makproductions.apocalypseweatherapp.view.ui.fragments.weather.list;

import ru.makproductions.apocalypseweatherapp.model.weather.repo.WeatherResult;

public interface WeatherListListener {
    void onListItemClick(WeatherResult result);
}
