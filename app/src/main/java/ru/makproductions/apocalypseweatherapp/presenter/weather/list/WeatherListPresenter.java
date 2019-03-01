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
        Timber.d("WeatherListPresenter Constructor");
        this.scheduler = scheduler;
    }

    @Override
    public void attachView(WeatherListView view) {
        super.attachView(view);
        this.cityListPresenter = new CityListPresenter();
        getViewState().init();
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
                    if (weatherResult != null
                            && weatherResult.getCityName() != null
                            && weatherResult.getWeatherDescription() != null) {
                        String cityName = weatherResult.getCityName();
                        double temp = weatherResult.getTemperature();
                        String description = weatherResult.getWeatherDescription();
                        cityWeatherRepo.saveCity(new City(cityName));
                        cityWeatherRepo.saveCityWeather(new CityWeather(cityName, temp, description));
                        getViewState().onListItemClick(weatherResult);
                    } else {
                        onError(new NullPointerException("Weather result is empty"));
                    }
                }

                @Override
                public void onError(Throwable e) {
                    onLoadFailed();
                    Timber.e(e);
                }
            };
            Timber.d("cityWeatherRepo=" + cityWeatherRepo);
            Timber.d("scheduler=" + scheduler);
            Timber.d("observer=" + singleObserver);
            cityWeatherRepo.loadWeather(townSelectedToShow, locale)
                    .observeOn(scheduler)
                    .subscribe(singleObserver);
        }

        public void onLoadFailed() {
            getViewState().onLoadFailed();
        }

        @Override
        public int getCitiesCount() {
            return 0;
        }
    }
}
