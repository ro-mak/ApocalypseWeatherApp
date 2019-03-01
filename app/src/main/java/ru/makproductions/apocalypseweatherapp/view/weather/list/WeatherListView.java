package ru.makproductions.apocalypseweatherapp.view.weather.list;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface WeatherListView extends MvpView {
    void init();
    void onListItemClick(WeatherResult result);

    void onLoadFailed();
}
