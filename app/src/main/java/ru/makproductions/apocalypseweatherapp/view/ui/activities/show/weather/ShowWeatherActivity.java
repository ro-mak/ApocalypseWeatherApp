package ru.makproductions.apocalypseweatherapp.view.ui.activities.show.weather;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.presenter.show.weather.ShowWeatherActivityPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.view.show.weather.ShowWeatherActivityView;
import ru.makproductions.apocalypseweatherapp.view.ui.fragments.show.weather.ShowWeatherFragment;
import timber.log.Timber;

//Activity for details on phones
public class ShowWeatherActivity extends MvpAppCompatActivity implements ShowWeatherActivityView {

    @InjectPresenter
    ShowWeatherActivityPresenter presenter;

    @ProvidePresenter
    public ShowWeatherActivityPresenter provideShowWeatherActivityPresenter() {
        ShowWeatherActivityPresenter presenter = new ShowWeatherActivityPresenter(AndroidSchedulers.mainThread());
        Timber.e("presenter created");
        return presenter;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        initActionBar();
        presenter.placeShowWeatherFragment();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.logo_layout);
            View customView = actionBar.getCustomView();
            TextView titleView = customView.findViewById(R.id.title);
            UtilMethods.changeFontTextView(titleView);
        }
    }

    @Override
    public void placeShowWeatherFragment() {
        ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.init(getIntent().getExtras());
        android.support.v4.app.FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.show_weather, showWeatherFragment);
        transaction.commit();
    }
}
