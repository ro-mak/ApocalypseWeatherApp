package ru.makproductions.apocalypseweatherapp.model.image;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.widget.ImageView;

import java.io.File;

public interface IImageLoader {
    void loadInto(ImageView imageView, File file);

    void loadInto(ImageView imageView, @RawRes @DrawableRes @Nullable Integer resourceId);
}
