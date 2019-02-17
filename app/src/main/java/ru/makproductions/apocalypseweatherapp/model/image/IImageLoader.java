package ru.makproductions.apocalypseweatherapp.model.image;

import android.widget.ImageView;

import java.io.File;

public interface IImageLoader {
    void loadInto(ImageView imageView, File file);
}
