package ru.makproductions.apocalypseweatherapp.model.network;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.weather.WeatherParser;
import ru.makproductions.apocalypseweatherapp.model.weather.map.WeatherMap;
import timber.log.Timber;

public class WeatherLoader {

    private static IRetrofitWeatherLoader weatherLoader;
    private WeatherLoader() {
    }

    public static Single<WeatherResult> loadWeather(int townSelectedToShow, String units, String appId, Locale locale) {
        CitiesHandler citiesHandler = CitiesHandler.getInstance();
        Timber.e("LoadWeather");
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.e(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(App.getInstance().getResources().getString(R.string.open_weather_map_api_url))
                .client(okHttpClient.build())
                .build();

        weatherLoader = retrofit.create(IRetrofitWeatherLoader.class);
        return Single.create((SingleOnSubscribe<WeatherResult>) emitter -> {
            Timber.e("Loading...");
            String language = "en";
            if (locale.getCountry().equals("RU")) language = "ru";
            Timber.e("Locale = " + locale.getCountry());
            String cityToFind = citiesHandler.getCitiesToFind().get(townSelectedToShow);
            WeatherMap weatherMap = weatherLoader.loadWeather(cityToFind, units, appId, language).blockingGet();
            if (weatherMap != null) {
                emitter.onSuccess(WeatherParser.parseWeatherMap(weatherMap, townSelectedToShow, citiesHandler));
            } else {
                emitter.onError(new NullPointerException("WeatherMap == null. Load from network failed."));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
