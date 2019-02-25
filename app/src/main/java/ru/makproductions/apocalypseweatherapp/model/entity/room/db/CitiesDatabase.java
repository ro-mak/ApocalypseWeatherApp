package ru.makproductions.apocalypseweatherapp.model.entity.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCity;
import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.room.dao.CitiesDao;
import ru.makproductions.apocalypseweatherapp.model.entity.room.dao.WeatherDao;

@Database(entities = {RoomCity.class, RoomCityWeather.class}, version = 1)
public abstract class CitiesDatabase extends RoomDatabase {
    private static final String DB_NAME = "ApocalypseDatabase.db";
    private static volatile CitiesDatabase instance;

    public static synchronized CitiesDatabase getInstance() {
        if (instance == null) {
            throw new NullPointerException("Database has not been created. Please call create()");
        }
        return instance;
    }

    public static void create(Context context) {
        instance = Room.databaseBuilder(context, CitiesDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();

    }

    public abstract CitiesDao getCitiesDao();

    public abstract WeatherDao getImageDao();
}
