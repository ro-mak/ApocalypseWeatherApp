package ru.makproductions.apocalypseweatherapp.di;

import dagger.Component;
import ru.makproductions.apocalypseweatherapp.CacheInstrumentedTest;
import ru.makproductions.apocalypseweatherapp.CityWeatherRepoInstrumentedTest;
import ru.makproductions.apocalypseweatherapp.di.modules.AppModule;
import ru.makproductions.apocalypseweatherapp.di.modules.CacheModule;
import ru.makproductions.apocalypseweatherapp.di.modules.NetApiModule;
import ru.makproductions.apocalypseweatherapp.di.modules.RepoModule;

@Component(modules = {AppModule.class, CacheModule.class, NetApiModule.class, RepoModule.class})
public interface TestComponent {
    void inject(CacheInstrumentedTest test);

    void inject(CityWeatherRepoInstrumentedTest test);
}
