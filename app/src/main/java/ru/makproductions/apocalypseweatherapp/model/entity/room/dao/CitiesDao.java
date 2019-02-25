package ru.makproductions.apocalypseweatherapp.model.entity.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCity;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CitiesDao {
    @Insert(onConflict = REPLACE)
    void insert(RoomCity city);

    @Insert(onConflict = REPLACE)
    void insert(RoomCity... cities);

    @Insert(onConflict = REPLACE)
    void insert(List<RoomCity> cities);

    @Update
    void update(RoomCity city);

    @Update
    void update(RoomCity... cities);

    @Update
    void update(List<RoomCity> cities);

    @Delete
    void delete(RoomCity city);

    @Delete
    void delete(RoomCity... cities);

    @Delete
    void delete(List<RoomCity> cities);

    @Query("SELECT * FROM roomcity")
    List<RoomCity> getAll();

    @Query("SELECT * FROM roomcity WHERE name = :name LIMIT 1")
    RoomCity findCity(String name);
}
