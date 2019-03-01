package ru.makproductions.apocalypseweatherapp.di.module;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import ru.makproductions.apocalypseweatherapp.model.repo.ICityWeatherRepo;
import timber.log.Timber;

@Module
public class TestWeatherCityRepoModule {
    @Provides
    public ICityWeatherRepo getCityWeatherRepo() {
        ICityWeatherRepo cityWeatherRepo = Mockito.mock(ICityWeatherRepo.class);
        Timber.d("Providing CityWeatherRepo " + cityWeatherRepo);
        return cityWeatherRepo;
    }
}
