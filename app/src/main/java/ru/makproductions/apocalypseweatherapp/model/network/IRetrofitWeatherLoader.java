package ru.makproductions.apocalypseweatherapp.model.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.makproductions.apocalypseweatherapp.model.weather.map.WeatherMap;

public interface IRetrofitWeatherLoader {
    @GET("/data/2.5/weather")
    Single<WeatherMap> loadWeather(@Query("q") String cityName, @Query("units") String units, @Query("appid") String appId, @Query("lang") String language);
}
