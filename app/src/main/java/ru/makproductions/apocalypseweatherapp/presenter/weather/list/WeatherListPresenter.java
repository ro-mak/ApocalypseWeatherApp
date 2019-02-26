package ru.makproductions.apocalypseweatherapp.presenter.weather.list;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.makproductions.apocalypseweatherapp.model.entity.City;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.repo.ICityWeatherRepo;
import ru.makproductions.apocalypseweatherapp.view.cities.recycler.CityListItemView;
import ru.makproductions.apocalypseweatherapp.view.weather.list.WeatherListView;
import timber.log.Timber;

@InjectViewState
public class WeatherListPresenter extends MvpPresenter<WeatherListView> {

    private CityListPresenter cityListPresenter;
    private Scheduler scheduler;
    @Inject
    ICityWeatherRepo cityWeatherRepo;

    public WeatherListPresenter(Scheduler scheduler) {
        this.cityListPresenter = new CityListPresenter();
        this.scheduler = scheduler;
    }

    public CityListPresenter getCityListPresenter() {
        return cityListPresenter;
    }

    public class CityListPresenter implements ICityListPresenter {

        @Override
        public void bindView(CityListItemView view) {

        }

        @Override
        public void loadWeather(int townSelectedToShow, Locale locale) {
            Timber.e("LoadWeather");
            SingleObserver<WeatherResult> singleObserver = new SingleObserver<WeatherResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(WeatherResult weatherResult) {
                    String cityName = weatherResult.getCityName();
                    double temp = weatherResult.getTemperature();
                    String description = weatherResult.getWeatherDescription();
                    cityWeatherRepo.saveCity(new City(cityName));
                    cityWeatherRepo.saveCityWeather(new CityWeather(cityName, temp, description));
                    getViewState().onListItemClick(weatherResult);
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e);
                }
            };
            cityWeatherRepo.loadWeather(townSelectedToShow, locale)
                    .observeOn(scheduler)
                    .subscribe(singleObserver);
        }

        @Override
        public int getCitiesCount() {
            return 0;
        }
    }
}
