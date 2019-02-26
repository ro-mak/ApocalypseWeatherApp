package ru.makproductions.apocalypseweatherapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.makproductions.apocalypseweatherapp.model.cache.ICache;
import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCache;

@Module
public class CacheModule {
    @Provides
    public ICache getCache() {
        return new RoomCache();
    }
}
