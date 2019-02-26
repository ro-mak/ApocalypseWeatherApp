package ru.makproductions.apocalypseweatherapp.view.ui.fragments.show.weather;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.weather.WeatherParser;
import ru.makproductions.apocalypseweatherapp.presenter.show.weather.ShowWeatherFragmentPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.view.show.weather.ShowWeatherFragmentView;
import ru.makproductions.apocalypseweatherapp.view.ui.fragments.apocalypse.countdown.ApocalypseCountdownFragment;
import ru.makproductions.apocalypseweatherapp.view.ui.fragments.weather.details.WeatherDetailsFragment;
import timber.log.Timber;

//Fragment for weather details
public class ShowWeatherFragment extends MvpAppCompatFragment implements ShowWeatherFragmentView {

    @SuppressWarnings("HardCodedStringLiteral")
    private static final String APOCALYPSE_COUNTDOWN_FRAGMENT = "APOCALYPSE_COUNTDOWN_FRAGMENT";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String DATE_OF_DOOM = "DATE_OF_DOOM";

    private static final long TIME_TO_APOCALYPSE = 1546290000000L;
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String TAG = "ShowWeatherFragment!!!";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String WEATHER_DETAILS_FRAGMENT = "WEATHER_DETAILS_FRAGMENT";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String ON_CREATE_VIEW_SHOW_WEATHER_FRAGMENT_WEATHER_RESULT_WEATHER = "onCreateView: ShowWeatherFragment: weatherResult == null";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String FRAGMENT_ACTIVITY_IS_NULL = "FragmentActivity is null";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TEXT_PLAIN = "text/plain";
    private String weather_message;
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEATHER_MESSAGE = "weather_message";
    private WeatherResult weatherResult;

    @InjectPresenter
    ShowWeatherFragmentPresenter presenter;

    @ProvidePresenter
    public ShowWeatherFragmentPresenter provideShowWeatherFragmentPresenter() {
        ShowWeatherFragmentPresenter presenter = new ShowWeatherFragmentPresenter(AndroidSchedulers.mainThread());
        Timber.e("presenter created");
        return presenter;
    }

    public static ShowWeatherFragment init(Bundle bundle) {
        ShowWeatherFragment showWeatherFragment = new ShowWeatherFragment();
        if (bundle != null) {
            showWeatherFragment.setArguments(bundle);
        } else {
            throw new NullPointerException("ShowWeatherFragment init bundle null");
        }
        return showWeatherFragment;
    }

    @BindView(R.id.show_weather_textview)
    TextView showWeatherTextView;
    @BindView(R.id.weather_image)
    ImageView weatherImage;
    @BindView(R.id.share_weather_button)
    Button shareWeatherButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_weather_fragment, container, false);
        ButterKnife.bind(this, view);
        presenter.loadWeatherResult();
        setFonts();
        createNestedFragment();
        presenter.setWeatherMessage();
        return view;
    }

    @Override
    public void loadWeatherResult() {
        Bundle args = this.getArguments();
        if (args != null && args.getParcelable(WEATHER_MESSAGE) != null) {
            weatherResult = args.getParcelable(WEATHER_MESSAGE);
            if (weatherResult != null) {
                weather_message = presenter.getWeather(weatherResult);
            } else {
                Timber.e(ON_CREATE_VIEW_SHOW_WEATHER_FRAGMENT_WEATHER_RESULT_WEATHER);
            }
        }
    }


    @Override
    public void setWeatherMessage() {
        if (weather_message != null) {
            showWeatherTextView.setText(weather_message.replaceAll("_", " "));
            WeatherParser.setWeatherImage(weatherImage, weather_message);
        }
    }

    private void setFonts() {
        UtilMethods.changeFontTextView(showWeatherTextView);
        UtilMethods.changeFontTextView(shareWeatherButton);
    }

    private void createNestedFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        ApocalypseCountdownFragment countdownFragment =
                (ApocalypseCountdownFragment) fragmentManager.findFragmentByTag(APOCALYPSE_COUNTDOWN_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putLong(DATE_OF_DOOM, TIME_TO_APOCALYPSE);
        if (countdownFragment == null) {
            countdownFragment = ApocalypseCountdownFragment.init(bundle);
            createFragment(fragmentManager, countdownFragment, APOCALYPSE_COUNTDOWN_FRAGMENT, R.id.countdown_container);
        }
        WeatherDetailsFragment weatherDetailsFragment =
                (WeatherDetailsFragment) fragmentManager.findFragmentByTag(WEATHER_DETAILS_FRAGMENT);
        if (weatherDetailsFragment == null) {
            bundle.putParcelable(WEATHER_MESSAGE, weatherResult);
            weatherDetailsFragment = WeatherDetailsFragment.init(bundle);
            createFragment(fragmentManager, weatherDetailsFragment,
                    WEATHER_DETAILS_FRAGMENT, R.id.weather_details_container);
        }
    }

    private void createFragment(FragmentManager fragmentManager, Fragment fragment, String tag, int resourceId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resourceId, fragment, tag);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.share_weather_button)
    public void onShareWeatherButtonClick(View view) {
        if (view.getId() == R.id.share_weather_button) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(TEXT_PLAIN);
            intent.putExtra(Intent.EXTRA_TEXT, weather_message + "\n" + weatherResult.getWeekForecast());
            FragmentActivity activity = getActivity();
            if (activity == null) throw new RuntimeException(TAG + FRAGMENT_ACTIVITY_IS_NULL);
            PackageManager packageManager = activity.getPackageManager();
            if (!packageManager.queryIntentActivities(intent, 0).isEmpty()) {
                startActivity(intent);
                activity.setResult(FragmentActivity.RESULT_OK);
            } else {
                activity.setResult(FragmentActivity.RESULT_CANCELED);
            }
        }
    }
}