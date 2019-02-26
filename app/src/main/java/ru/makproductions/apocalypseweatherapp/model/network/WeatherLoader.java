package ru.makproductions.apocalypseweatherapp.model.network;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.weather.WeatherParser;
import ru.makproductions.apocalypseweatherapp.model.weather.map.WeatherMap;
import timber.log.Timber;

public class WeatherLoader {

    private IRetrofitWeatherLoader weatherLoader;

    public WeatherLoader(IRetrofitWeatherLoader weatherLoader) {
        this.weatherLoader = weatherLoader;
    }

    public Single<WeatherResult> loadWeather(int townSelectedToShow, String units, String appId, Locale locale) {
        CitiesHandler citiesHandler = CitiesHandler.getInstance();
        Timber.e("LoadWeather");
        return Single.create((SingleOnSubscribe<WeatherResult>) emitter -> {
            Timber.e("Loading...");
            String language = "en";
            if (locale.getCountry().equals("RU")) language = "ru";
            Timber.e("Locale = %s", locale.getCountry());
            String cityToFind = citiesHandler.getCitiesToFind().get(townSelectedToShow);
            WeatherMap weatherMap = weatherLoader.loadWeather(cityToFind, units, appId, language).blockingGet();
            if (weatherMap != null) {
                emitter.onSuccess(WeatherParser.parseWeatherMap(weatherMap, townSelectedToShow, citiesHandler));
            } else {
                emitter.onError(new NullPointerException("WeatherMap == null. Load from network failed."));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
