package ru.makproductions.apocalypseweatherapp.model.entity.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RoomCity {
    @NonNull
    @PrimaryKey
    private String name;

    public RoomCity() {
    }

    public RoomCity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
