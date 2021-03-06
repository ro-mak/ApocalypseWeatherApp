package ru.makproductions.apocalypseweatherapp.presenter.show.weather;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.show.weather.ShowWeatherFragmentView;

@InjectViewState
public class ShowWeatherFragmentPresenter extends MvpPresenter<ShowWeatherFragmentView> {

    private Scheduler scheduler;

    public ShowWeatherFragmentPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setWeatherMessage() {
        getViewState().setWeatherMessage();
    }

    public void loadWeatherResult() {
        getViewState().loadWeatherResult();
    }
}
