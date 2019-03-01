package ru.makproductions.apocalypseweatherapp;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import ru.makproductions.apocalypseweatherapp.di.DaggerTestComponent;
import ru.makproductions.apocalypseweatherapp.di.TestComponent;
import ru.makproductions.apocalypseweatherapp.di.modules.AppModule;
import ru.makproductions.apocalypseweatherapp.di.modules.CacheModule;
import ru.makproductions.apocalypseweatherapp.di.modules.NetApiModule;
import ru.makproductions.apocalypseweatherapp.model.cache.ICache;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.entity.room.RoomCache;
import ru.makproductions.apocalypseweatherapp.model.repo.ICityWeatherRepo;
import ru.makproductions.apocalypseweatherapp.model.weather.map.Main;
import ru.makproductions.apocalypseweatherapp.model.weather.map.Weather;
import ru.makproductions.apocalypseweatherapp.model.weather.map.WeatherMap;
import timber.log.Timber;

import static org.junit.Assert.assertEquals;

public class CityWeatherRepoInstrumentedTest {
    private static MockWebServer mockWebServer;
    @Inject
    ICityWeatherRepo cityWeatherRepo;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void setup() {
        TestComponent component = DaggerTestComponent
                .builder()
                .netApiModule(new NetApiModule() {
                    @Override
                    @Named("baseUrl")
                    public String getApiBaseUrl(App app) {
                        return mockWebServer.url("/").toString();
                    }
                })
                .cacheModule(new CacheModule() {
                    @Override
                    public ICache getCache() {
                        return Mockito.mock(RoomCache.class);
                    }
                })
                .appModule(new AppModule(App.getInstance()))
                .build();

        component.inject(this);
    }

    @Test
    public void loadWeatherTest() {
        mockWebServer.enqueue(createWeatherResponse(mockWeatherMap()));
        TestObserver<WeatherResult> observer = new TestObserver<>();
        cityWeatherRepo.loadWeather(0, Locale.ENGLISH).subscribe(observer);
        observer.awaitTerminalEvent();
        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getWeatherDescription(), "some description");
        assertEquals(observer.values().get(0).getCityName(), "moscow");
        assertEquals(observer.values().get(0).getTemperature(), 5.0, 0);
    }

    private WeatherMap mockWeatherMap() {
        Main main = new Main();
        main.setTemp(5.0);
        Weather weather = new Weather();
        weather.setDescription("some description");
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(weather);
        WeatherMap weatherMap = new WeatherMap();
        weatherMap.setName("moscow");
        weatherMap.setMain(main);
        weatherMap.setWeather(weatherList);
        return weatherMap;
    }

    private MockResponse createWeatherResponse(WeatherMap weatherMap) {
        Weather weather = weatherMap.getWeather().get(0);
        Main main = weatherMap.getMain();
        String body = "{" +
                "\"coord\":{" +
                "\"lon\":37.62, \"lat\":55.75" +
                "},\"weather\":[{" +
                "\"id\":" + weather.getId()
                + ",\"main\":\"Snow\"," +
                " \"description\":\"" + weather.getDescription() + "\", \"icon\":\"13d\"" +
                "}],\"base\":\"stations\", " +
                "\"main\":{" +
                "\"temp\": " + main.getTemp() + ", \"pressure\":986, \"humidity\":74, \"temp_min\":1, \"temp_max\":2.78" +
                "},\"visibility\":10000, \"wind\":{" +
                "\"speed\":3, \"deg\":50" +
                "},\"clouds\":{" +
                "\"all\":75" +
                "},\"dt\":1551439997, \"sys\":{" +
                "\"type\":1, \"id\":9029, \"message\":0.0047, \"country\":\"RU\", \"sunrise\":1551414032, \"sunset\":" +
                "1551452647" +
                "},\"id\":524901, " +
                "\"name\":\"" + weatherMap.getName() + "\", \"cod\":200}";
        Timber.e(body);
        return new MockResponse().setBody(body);
    }

}
