package ru.makproductions.apocalypseweatherapp.di;

import dagger.Component;
import ru.makproductions.apocalypseweatherapp.di.modules.AppModule;
import ru.makproductions.apocalypseweatherapp.di.modules.CacheModule;
import ru.makproductions.apocalypseweatherapp.di.modules.ImageModule;
import ru.makproductions.apocalypseweatherapp.di.modules.NetApiModule;
import ru.makproductions.apocalypseweatherapp.di.modules.RepoModule;
import ru.makproductions.apocalypseweatherapp.presenter.weather.list.WeatherListPresenter;
import ru.makproductions.apocalypseweatherapp.view.ui.activities.main.MainActivity;
import ru.makproductions.apocalypseweatherapp.view.ui.fragments.weather.list.WeatherListFragment;

@Component(modules = {AppModule.class, RepoModule.class, CacheModule.class, NetApiModule.class, ImageModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(WeatherListPresenter weatherListPresenter);

    void inject(WeatherListFragment weatherListFragment);
}
