package ru.makproductions.apocalypseweatherapp.view.apocalypse.countdown;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ApocalypseCountdownView extends MvpView {
}
