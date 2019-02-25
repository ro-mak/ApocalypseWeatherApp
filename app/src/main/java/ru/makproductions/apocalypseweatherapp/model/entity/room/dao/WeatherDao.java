package ru.makproductions.apocalypseweatherapp.model.entity.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCityWeather;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {
    @Insert(onConflict = REPLACE)
    void insert(RoomCityWeather weather);

    @Update
    void update(RoomCityWeather weather);

    @Delete
    void delete(RoomCityWeather weather);

    @Query("SELECT * FROM roomcityweather WHERE cityName = :city LIMIT 1")
    RoomCityWeather findForCity(String city);
}
