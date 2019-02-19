package ru.makproductions.apocalypseweatherapp.view.show.weather;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ShowWeatherFragmentView extends MvpView {
    void setWeatherMessage();

    void loadWeatherResult();
}
