package ru.makproductions.apocalypseweatherapp.presenter.weather.list;

import java.util.Locale;

import ru.makproductions.apocalypseweatherapp.view.cities.recycler.CityListItemView;

public interface ICityListPresenter {
    void bindView(CityListItemView view);

    void loadWeather(int townSelectedToShow, Locale locale);

    int getCitiesCount();
}
