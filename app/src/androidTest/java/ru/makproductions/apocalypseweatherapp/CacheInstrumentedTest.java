package ru.makproductions.apocalypseweatherapp;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;

import ru.makproductions.apocalypseweatherapp.di.DaggerTestComponent;
import ru.makproductions.apocalypseweatherapp.di.TestComponent;
import ru.makproductions.apocalypseweatherapp.model.cache.ICache;
import ru.makproductions.apocalypseweatherapp.model.entity.City;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.room.db.CitiesDatabase;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;

public class CacheInstrumentedTest {
    @Inject
    ICache cache;

    @BeforeClass
    public static void setupClass() {
        Timber.plant(new Timber.DebugTree());
        Timber.d("setup class");
    }

    @Before
    public void setup() {
        Timber.plant(new Timber.DebugTree());
        TestComponent component = DaggerTestComponent
                .builder()
                .build();
        component.inject(this);
        Timber.d("setup");
        CitiesDatabase.getInstance().clearAllTables();
    }

    @Test
    public void saveCityWeatherTest() {
        CityWeather cityWeather = new CityWeather("Moscow", 5, "description");
        cache.saveCityWeather(cityWeather);
        CityWeather loadedCityWeather = cache.loadCityWeather(cityWeather.getCityName());
        assertEquals(cityWeather.getCityName(), loadedCityWeather.getCityName());
        assertEquals(cityWeather.getTemperature(), loadedCityWeather.getTemperature());
        assertEquals(cityWeather.getWeatherDescription(), loadedCityWeather.getWeatherDescription());
    }

    @Test
    public void saveCityTest() {
        City city = new City("Moscow");
        cache.saveCity(city);
        City loadedCity = cache.loadCity(city.getName());
        assertEquals(city.getName(), loadedCity.getName());
    }
}
