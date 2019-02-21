package ru.makproductions.apocalypseweatherapp.presenter.weather.list;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.weather.list.WeatherListView;

@InjectViewState
public class WeatherListPresenter extends MvpPresenter<WeatherListView> {
    private Scheduler scheduler;

    public WeatherListPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
