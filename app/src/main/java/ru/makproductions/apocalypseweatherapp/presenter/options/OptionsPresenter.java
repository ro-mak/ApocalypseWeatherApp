package ru.makproductions.apocalypseweatherapp.presenter.options;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.options.OptionsView;

@InjectViewState
public class OptionsPresenter extends MvpPresenter<OptionsView> {

    private Scheduler scheduler;

    public OptionsPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void changeAvatar() {
        getViewState().changeAvatar();
    }
}
