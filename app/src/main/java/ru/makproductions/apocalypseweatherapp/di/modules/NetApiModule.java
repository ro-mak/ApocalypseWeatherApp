package ru.makproductions.apocalypseweatherapp.di.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.network.IRetrofitWeatherLoader;
import ru.makproductions.apocalypseweatherapp.model.network.WeatherLoader;
import timber.log.Timber;

@Module
public class NetApiModule {
    @Named("baseUrl")
    @Provides
    public String getApiBaseUrl(App app) {
        return app.getResources().getString(R.string.open_weather_map_api_url);
    }

    @Provides
    public OkHttpClient.Builder getOkHttpClientBuilder(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor);
    }

    @Provides
    public IRetrofitWeatherLoader getRetrofitWeatherLoader(Gson gson, @Named("baseUrl") String baseUrl, OkHttpClient.Builder okHttpClientBuilder) {
        Timber.e("getApiService");
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClientBuilder.build())
                .build()
                .create(IRetrofitWeatherLoader.class);
    }

    @Provides
    public WeatherLoader getWeatherLoader(IRetrofitWeatherLoader retrofitWeatherLoader) {
        return new WeatherLoader(retrofitWeatherLoader);
    }

    @Provides
    public Gson getGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    @Provides
    public HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.e(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
