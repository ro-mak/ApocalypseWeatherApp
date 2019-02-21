package ru.makproductions.apocalypseweatherapp.presenter.weather.details;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.weather.details.WeatherDetailsView;

@InjectViewState
public class WeatherDetailsPresenter extends MvpPresenter<WeatherDetailsView> {
    private Scheduler scheduler;

    public WeatherDetailsPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
