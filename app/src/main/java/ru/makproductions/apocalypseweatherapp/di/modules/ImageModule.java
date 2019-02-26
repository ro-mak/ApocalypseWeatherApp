package ru.makproductions.apocalypseweatherapp.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.makproductions.apocalypseweatherapp.model.image.GlideIImageLoader;
import ru.makproductions.apocalypseweatherapp.model.image.IImageLoader;

@Module
public class ImageModule {
    @Provides
    public IImageLoader getImageLoader() {
        return new GlideIImageLoader();
    }
}
