package ru.makproductions.apocalypseweatherapp.presenter.show.weather;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.show.weather.ShowWeatherActivityView;

@InjectViewState
public class ShowWeatherActivityPresenter extends MvpPresenter<ShowWeatherActivityView> {

    private Scheduler scheduler;

    public ShowWeatherActivityPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void placeShowWeatherFragment() {
        getViewState().placeShowWeatherFragment();
    }
}
