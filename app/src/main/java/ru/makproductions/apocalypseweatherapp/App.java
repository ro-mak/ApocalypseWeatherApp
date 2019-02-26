package ru.makproductions.apocalypseweatherapp;

import android.app.Application;

import ru.makproductions.apocalypseweatherapp.di.AppComponent;
import ru.makproductions.apocalypseweatherapp.di.DaggerAppComponent;
import ru.makproductions.apocalypseweatherapp.di.modules.AppModule;
import ru.makproductions.apocalypseweatherapp.model.entity.room.db.CitiesDatabase;
import timber.log.Timber;

public class App extends Application {

    private static App instance;
    private AppComponent appComponent;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        CitiesDatabase.create(this);
        appComponent = DaggerAppComponent
                .builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
