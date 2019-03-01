package ru.makproductions.apocalypseweatherapp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;
import ru.makproductions.apocalypseweatherapp.di.DaggerTestComponent;
import ru.makproductions.apocalypseweatherapp.di.TestComponent;
import ru.makproductions.apocalypseweatherapp.di.module.TestWeatherCityRepoModule;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.repo.ICityWeatherRepo;
import ru.makproductions.apocalypseweatherapp.presenter.weather.list.WeatherListPresenter;
import ru.makproductions.apocalypseweatherapp.view.weather.list.WeatherListView;
import timber.log.Timber;

public class WeatherListPresenterUnitTest {
    @Mock
    WeatherListView weatherListView;
    private WeatherListPresenter presenter;
    private TestScheduler testScheduler;

    @BeforeClass
    public static void setupClass() {
        Timber.plant(new Timber.DebugTree());
        Timber.d("setup class");
    }

    @AfterClass
    public static void tearDownClass() {
        Timber.d("tear down class");
    }

    @Before
    public void setup() {
        Timber.d("setup");
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new WeatherListPresenter(testScheduler));
    }

    @After
    public void tearDown() {
        Timber.d("tearDown");
    }

    @Test
    public void loadWeatherTest() {
        WeatherResult weatherResult = new WeatherResult(5.0, "Moscow", "description");
        TestComponent component = DaggerTestComponent.builder()
                .testWeatherCityRepoModule(new TestWeatherCityRepoModule() {
                    @Override
                    public ICityWeatherRepo getCityWeatherRepo() {
                        ICityWeatherRepo repo = super.getCityWeatherRepo();
                        Mockito.when(repo.loadWeather(0, Locale.ENGLISH)).thenReturn(Single.just(weatherResult));
                        return repo;
                    }
                }).build();
        component.inject(presenter);
        presenter.attachView(weatherListView);
        presenter.getCityListPresenter().loadWeather(0, Locale.ENGLISH);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        Timber.d("TimeTravel ");
        Mockito.verify(weatherListView).onListItemClick(weatherResult);
    }

    @Test
    public void onLoadFailedTest() {
        WeatherResult weatherResult = new WeatherResult(5.0, "Moscow", "description");
        TestComponent component = DaggerTestComponent.builder()
                .testWeatherCityRepoModule(new TestWeatherCityRepoModule() {
                    @Override
                    public ICityWeatherRepo getCityWeatherRepo() {
                        ICityWeatherRepo repo = super.getCityWeatherRepo();
                        Mockito.when(repo.loadWeather(0, Locale.ENGLISH)).thenReturn(Single.just(new WeatherResult()));
                        return repo;
                    }
                }).build();
        component.inject(presenter);
        presenter.attachView(weatherListView);
        presenter.getCityListPresenter().loadWeather(0, Locale.ENGLISH);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        Timber.d("TimeTravel ");
        Mockito.verify(weatherListView).onLoadFailed();
    }
}
