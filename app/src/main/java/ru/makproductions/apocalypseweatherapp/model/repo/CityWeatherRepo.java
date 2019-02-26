package ru.makproductions.apocalypseweatherapp.model.repo;

import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.makproductions.apocalypseweatherapp.model.cache.ICache;
import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.model.entity.City;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCache;
import ru.makproductions.apocalypseweatherapp.model.network.NetworkStatus;
import ru.makproductions.apocalypseweatherapp.model.network.WeatherLoader;
import ru.makproductions.apocalypseweatherapp.model.weather.WeatherParser;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;
import timber.log.Timber;

enum DbEntity {CITY, CITY_WEATHER}

public class CityWeatherRepo implements ICityWeatherRepo {
    private ICache iCache = new RoomCache();

    @Override
    public Single<WeatherResult> loadWeather(int townSelectedToShow, Locale locale) {
        CitiesHandler citiesHandler = CitiesHandler.getInstance();
        String city = citiesHandler.getCitiesInEnglish().get(townSelectedToShow).toLowerCase();
        if (NetworkStatus.isOnline()) {
            return WeatherLoader.loadWeather(townSelectedToShow, UtilVariables.METRIC, UtilVariables.API_KEY, locale);
        } else {
            return Single.create((SingleOnSubscribe<WeatherResult>) emitter -> {
                CityWeather cityWeather = iCache.loadCityWeather(city);
                if (cityWeather != null) {
                    emitter.onSuccess(WeatherParser.parseCityWeather(cityWeather));
                } else {
                    emitter.onError(new NullPointerException("There is no weather saved for that city"));
                }
            }).subscribeOn(Schedulers.io());
        }
    }

    @Override
    public void saveCity(City city) {
        if (NetworkStatus.isOnline()) {
            saveToCacheOnIOThread(DbEntity.CITY, city);
        }
    }

    @Override
    public void saveCityWeather(CityWeather cityWeather) {
        if (NetworkStatus.isOnline()) {
            saveToCacheOnIOThread(DbEntity.CITY_WEATHER, cityWeather);
        }
    }

    private void saveToCacheOnIOThread(DbEntity entity, Object o) {
        Completable.create(emitter -> {
                    try {
                        if (entity.equals(DbEntity.CITY)) {
                            City city = (City) o;
                            if (city != null) {
                                iCache.saveCity(city);
                            } else {
                                emitter.onError(new NullPointerException("Cannot save the city because it is null"));
                            }
                        } else if (entity.equals(DbEntity.CITY_WEATHER)) {
                            CityWeather cityWeather = (CityWeather) o;
                            if (cityWeather != null) {
                                iCache.saveCityWeather(cityWeather);
                            } else {
                                emitter.onError(new NullPointerException("Cannot save the cityWeather because it is null"));
                            }
                        }
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                    emitter.onComplete();
                }
        ).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Timber.e("EntitySaved + %s", entity.toString());
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
            }
        });
    }
}
