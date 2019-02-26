package ru.makproductions.apocalypseweatherapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.makproductions.apocalypseweatherapp.App;

@Module
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public App getApp() {
        return app;
    }
}
