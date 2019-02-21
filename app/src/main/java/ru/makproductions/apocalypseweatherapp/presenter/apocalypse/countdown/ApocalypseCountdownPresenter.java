package ru.makproductions.apocalypseweatherapp.presenter.apocalypse.countdown;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.apocalypse.countdown.ApocalypseCountdownView;

@InjectViewState
public class ApocalypseCountdownPresenter extends MvpPresenter<ApocalypseCountdownView> {
    private Scheduler scheduler;

    public ApocalypseCountdownPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
