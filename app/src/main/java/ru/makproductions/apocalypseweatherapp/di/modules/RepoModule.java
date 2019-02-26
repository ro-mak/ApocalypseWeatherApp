package ru.makproductions.apocalypseweatherapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.makproductions.apocalypseweatherapp.model.cache.ICache;
import ru.makproductions.apocalypseweatherapp.model.network.WeatherLoader;
import ru.makproductions.apocalypseweatherapp.model.repo.CityWeatherRepo;
import ru.makproductions.apocalypseweatherapp.model.repo.ICityWeatherRepo;

@Module
public class RepoModule {
    @Provides
    public ICityWeatherRepo getCityWeatherRepo(WeatherLoader weatherLoader, ICache cache) {
        return new CityWeatherRepo(weatherLoader, cache);
    }
}
