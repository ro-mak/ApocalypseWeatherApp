package ru.makproductions.apocalypseweatherapp;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
    }
}
