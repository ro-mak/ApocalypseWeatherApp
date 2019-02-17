package ru.makproductions.apocalypseweatherapp.model.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class GlideIImageLoader implements IImageLoader {
    public void loadInto(ImageView imageView, File file) {
        Glide.with(imageView).load(file).into(imageView);
    }
}
