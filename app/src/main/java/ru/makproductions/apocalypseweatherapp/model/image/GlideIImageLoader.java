package ru.makproductions.apocalypseweatherapp.model.image;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class GlideIImageLoader implements IImageLoader {
    @Override
    public void loadInto(ImageView imageView, File file) {
        Glide.with(imageView).load(file).into(imageView);
    }

    @Override
    public void loadInto(ImageView imageView, @Nullable Integer resourceId) {
        Glide.with(imageView).load(resourceId).into(imageView);
    }
}
