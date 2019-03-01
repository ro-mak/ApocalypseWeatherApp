package ru.makproductions.apocalypseweatherapp.model.weather;

import android.content.res.Resources;
import android.os.Build;
import android.widget.ImageView;

import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.model.entity.CityWeather;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.image.GlideIImageLoader;
import ru.makproductions.apocalypseweatherapp.model.image.IImageLoader;
import ru.makproductions.apocalypseweatherapp.model.weather.map.WeatherMap;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

public class WeatherParser {
    private static final int MIN_HEIGHT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 300 : 492;
    private static final int MIN_WIDTH = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 300 : 492;
    private static final int WEATHER_ARRAY_INDEX = 0;
    private static IImageLoader imageLoader = new GlideIImageLoader();

    public static void setWeatherImage(ImageView weatherImage, String weather_message) {
        Resources resources = App.getInstance().getResources();
        String[] parsedMessage = weather_message.split(" ");
        String tempString;
        for (int i = 0; i < parsedMessage.length; i++) {
            tempString = parsedMessage[i].toLowerCase();
            if (tempString.contains(resources.getString(R.string.clear_sky))) {
                imageLoader.loadInto(weatherImage, R.drawable.sunny);
                break;
            } else if (tempString.contains(resources.getString(R.string.drizzle)) || tempString.contains(resources.getString(R.string.rain))
                    && !tempString.contains(resources.getString(R.string.snow))) {
                imageLoader.loadInto(weatherImage, R.drawable.raining);
                break;
            } else if (!tempString.contains(resources.getString(R.string.rain)) && tempString.contains(resources.getString(R.string.snow))) {
                imageLoader.loadInto(weatherImage, R.drawable.snowing);
                break;
            } else if (tempString.contains(resources.getString(R.string.broken_clouds))) {
                imageLoader.loadInto(weatherImage, R.drawable.broken_clouds);
                break;
            } else if (tempString.contains(resources.getString(R.string.clouds)) && !tempString.contains(resources.getString(R.string.broken_clouds))) {
                imageLoader.loadInto(weatherImage, R.drawable.cloudy);
                break;
            } else if (tempString.contains(resources.getString(R.string.thunder_storm))) {
                imageLoader.loadInto(weatherImage, R.drawable.rainstorm);
                break;
            } else if (tempString.contains(resources.getString((R.string.fog)))) {
                break;
            } else if (tempString.contains(resources.getString(R.string.rain)) && tempString.contains(resources.getString(R.string.snow))) {
                imageLoader.loadInto(weatherImage, R.drawable.rain_with_snow);
                break;
            }
        }
        weatherImage.setMinimumHeight(MIN_HEIGHT);
        weatherImage.setMinimumWidth(MIN_WIDTH);
    }

    public static WeatherResult parseWeatherMap(WeatherMap weatherMap, int position, CitiesHandler citiesHandler) {
        WeatherResult weatherResult = new WeatherResult();
        String city = citiesHandler.getCitiesInEnglish().get(position).toLowerCase();
        UtilMethods.formatCityName(city);
        double temp = weatherMap.getMain().getTemp();
        String description = weatherMap.getWeather().get(WEATHER_ARRAY_INDEX).getDescription();
        weatherResult.setCityName(city);
        weatherResult.setTemperature(temp);
        weatherResult.setWeatherDescription(city + " " + temp + " " + description);
        return weatherResult;
    }

    public static WeatherResult parseCityWeather(CityWeather cityWeather) {
        WeatherResult weatherResult = new WeatherResult();
        String city = cityWeather.getCityName();
        double temp = cityWeather.getTemperature();
        String description = cityWeather.getWeatherDescription();
        weatherResult.setCityName(city);
        weatherResult.setTemperature(temp);
        weatherResult.setWeatherDescription(description);
        return weatherResult;
    }
}
