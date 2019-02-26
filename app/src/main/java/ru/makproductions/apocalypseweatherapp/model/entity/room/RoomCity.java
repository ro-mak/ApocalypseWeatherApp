package ru.makproductions.apocalypseweatherapp.model.entity.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RoomCity {
    @NonNull
    @PrimaryKey
    private String name;

    @Ignore
    public RoomCity() {
    }

    public RoomCity(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }


}
