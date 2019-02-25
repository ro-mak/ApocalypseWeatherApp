package ru.makproductions.apocalypseweatherapp.presenter.weather.list;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.network.WeatherLoader;
import ru.makproductions.apocalypseweatherapp.model.weather.map.WeatherMap;
import ru.makproductions.apocalypseweatherapp.presenter.main.ICityListPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;
import ru.makproductions.apocalypseweatherapp.view.cities.recycler.CityListItemView;
import ru.makproductions.apocalypseweatherapp.view.weather.list.WeatherListView;
import timber.log.Timber;

@InjectViewState
public class WeatherListPresenter extends MvpPresenter<WeatherListView> {
    private static final int WEATHER_ARRAY_INDEX = 0;
    private CityListPresenter cityListPresenter;
    private Scheduler scheduler;

    public WeatherListPresenter(Scheduler scheduler) {
        this.cityListPresenter = new CityListPresenter();
        this.scheduler = scheduler;
    }

    public CityListPresenter getCityListPresenter() {
        return cityListPresenter;
    }

    private WeatherResult updateWeatherData(WeatherMap weatherMap, int position, CitiesHandler citiesHandler) {
        WeatherResult weatherResult = WeatherResult.getInstance();
        final String city = citiesHandler.getCitiesInEnglish().get(position).toLowerCase();
        UtilMethods.formatCityName(city);
        weatherResult.setWeather(city + " " + weatherMap.getMain().getTemp()
                + " " + weatherMap.getWeather().get(WEATHER_ARRAY_INDEX).getDescription());
        return weatherResult;
    }

    public class CityListPresenter implements ICityListPresenter {

        @Override
        public void bindView(CityListItemView view) {

        }

        @Override
        public void loadWeather(int townSelectedToShow, CitiesHandler citiesHandler, Locale locale) {
            Timber.e("LoadWeather");
            SingleObserver<WeatherMap> singleObserver = new SingleObserver<WeatherMap>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(WeatherMap weatherMap) {
                    getViewState().onListItemClick(updateWeatherData(weatherMap, townSelectedToShow, citiesHandler));
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e);
                }
            };
            String city = citiesHandler.getCitiesToFind().get(townSelectedToShow);
            Timber.e("GET_WEATHER_DESCRIPTION" + "CITY_TO_SEARCH " + city);
            WeatherLoader.loadWeather(city, UtilVariables.METRIC, UtilVariables.API_KEY, locale)
                    .observeOn(scheduler)
                    .subscribe(singleObserver);
        }

        @Override
        public int getCitiesCount() {
            return 0;
        }
    }
}
