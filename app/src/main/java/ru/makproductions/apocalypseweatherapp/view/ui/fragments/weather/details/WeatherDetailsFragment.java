package ru.makproductions.apocalypseweatherapp.view.ui.fragments.weather.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.entity.WeatherResult;
import ru.makproductions.apocalypseweatherapp.model.weather.WeatherParser;
import ru.makproductions.apocalypseweatherapp.presenter.weather.details.WeatherDetailsPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.view.weather.details.WeatherDetailsView;
import timber.log.Timber;

public class WeatherDetailsFragment extends MvpAppCompatFragment implements WeatherDetailsView {

    public static final boolean HAS_FIXED_SIZE_TRUE = true;
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEATHER_MESSAGE = "weather_message";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TAG = "##WDFragment##";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String FRAGMENT_ACTIVITY_NULL = " FragmentActivity == null";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEATHER_DETAILS_FRAGMENT_WEATHER_RESULT_IS_NULL = "WeatherDetailsFragment!!! weatherResult is null";
    private WeatherResult weatherResult;
    @BindView(R.id.details_title)
    TextView titleText;
    @BindView(R.id.forecast_recycler_view)
    RecyclerView forecastRecyclerView;

    @InjectPresenter
    WeatherDetailsPresenter presenter;

    @ProvidePresenter
    public WeatherDetailsPresenter provideWeatherDetailsPresenter() {
        WeatherDetailsPresenter presenter = new WeatherDetailsPresenter(AndroidSchedulers.mainThread());
        Timber.e("presenter created");
        return presenter;
    }

    public static WeatherDetailsFragment init(Bundle bundle) {
        WeatherDetailsFragment weatherDetailsFragment = new WeatherDetailsFragment();
        if (bundle != null) {
            weatherDetailsFragment.setArguments(bundle);
        }
        return weatherDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_details_fragment, container, false);
        ButterKnife.bind(this, rootView);
        initForecastRecycler();
        setFonts();
        return rootView;
    }

    private void setFonts() {
        UtilMethods.changeFontTextView(titleText);
    }

    private void initForecastRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        forecastRecyclerView.setLayoutManager(layoutManager);
        forecastRecyclerView.setHasFixedSize(HAS_FIXED_SIZE_TRUE);
        Bundle args = getArguments();
        if (args != null) {
            weatherResult = args.getParcelable(WEATHER_MESSAGE);
        }
        if (weatherResult != null) {
            List<String> weekForecast = weatherResult.getWeekForecast();
            //Timber.d(weatherResult.getWeekForecast().toString());
            if (weekForecast != null) {
                forecastRecyclerView.setAdapter(new ForecastRecyclerViewAdapter(weekForecast));
            }
        } else {
            // throw new NullPointerException(WEATHER_DETAILS_FRAGMENT_WEATHER_RESULT_IS_NULL);
        }
    }

    @OnClick(R.id.details_title)
    public void onTitleTextClick(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        Menu menu = popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, menu);
        UtilMethods.changeFontMenu(menu);
        popupMenu.show();
    }

    private class ForecastRecyclerViewAdapter
            extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {
        private List<String> forecastList;

        ForecastRecyclerViewAdapter(List<String> forecastList) {
            this.forecastList = forecastList;
        }

        @NonNull
        @Override
        public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ForecastViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
            holder.forecastTextView.setText(forecastList.get(position).replaceAll("_", " "));
            holder.setForecastImageView(position);
        }

        @Override
        public int getItemCount() {
            return forecastList.size();
        }

        class ForecastViewHolder extends RecyclerView.ViewHolder {
            @SuppressWarnings("HardCodedStringLiteral")
            private static final String TAG = "ForecastViewHolder ";
            private TextView forecastTextView;
            private ImageView forecastImageView;

            ForecastViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.forecast_list_item, parent, false));

                forecastTextView = itemView.findViewById(R.id.forecast_textview);
                FragmentActivity activity = getActivity();
                if (activity == null) throw new RuntimeException(TAG + FRAGMENT_ACTIVITY_NULL);
                UtilMethods.changeFontTextView(forecastTextView);
                forecastImageView = itemView.findViewById(R.id.forecast_image_view);
            }

            void setForecastImageView(int position) {
                WeatherParser.setWeatherImage(forecastImageView, forecastList.get(position));
            }
        }
    }
}
