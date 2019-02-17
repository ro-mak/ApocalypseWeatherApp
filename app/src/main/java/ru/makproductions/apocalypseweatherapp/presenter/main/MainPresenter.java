package ru.makproductions.apocalypseweatherapp.presenter.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.File;

import io.reactivex.Scheduler;
import ru.makproductions.apocalypseweatherapp.view.main.MainView;
import timber.log.Timber;

import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.ON_ACTIVITY_RESULT;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private Scheduler scheduler;

    public MainPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void loadAvatar(String avPath) {
        File file = new File(avPath);
        getViewState().loadAvatar(file);
        Timber.d("%s%s", ON_ACTIVITY_RESULT, file);
    }

    public void changeAvatar(String path) {
        Timber.d("changeAvatar: Start");
        getViewState().changeAvatar(path);
    }
}
