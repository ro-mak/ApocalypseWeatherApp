package ru.makproductions.apocalypseweatherapp.di;

import dagger.Component;
import ru.makproductions.apocalypseweatherapp.di.module.TestWeatherCityRepoModule;
import ru.makproductions.apocalypseweatherapp.presenter.weather.list.WeatherListPresenter;

@Component(modules = TestWeatherCityRepoModule.class)
public interface TestComponent {
    void inject(WeatherListPresenter presenter);
}
