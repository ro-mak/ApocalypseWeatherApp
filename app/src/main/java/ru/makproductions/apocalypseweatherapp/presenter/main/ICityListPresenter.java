package ru.makproductions.apocalypseweatherapp.presenter.main;

import java.util.Locale;

import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.view.recycler.CityListItemView;

public interface ICityListPresenter {
    void bindView(CityListItemView view);

    void loadWeather(int townSelectedToShow, CitiesHandler citiesHandler, Locale locale);

    int getCitiesCount();
}
