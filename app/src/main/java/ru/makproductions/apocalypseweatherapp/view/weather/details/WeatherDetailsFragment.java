package ru.makproductions.apocalypseweatherapp.view.weather.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.List;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.weather.repo.WeatherResult;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

public class WeatherDetailsFragment extends Fragment {

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
    private TextView titleText;

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
        titleText = rootView.findViewById(R.id.details_title);
        FragmentActivity activity = getActivity();
        if (activity == null) throw new RuntimeException(TAG + FRAGMENT_ACTIVITY_NULL);
        UtilMethods.changeFontTextView(titleText, activity);
        RecyclerView forecastRecyclerView = rootView.findViewById(R.id.forecast_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        forecastRecyclerView.setLayoutManager(layoutManager);
        forecastRecyclerView.setHasFixedSize(HAS_FIXED_SIZE_TRUE);
        Bundle args = getArguments();
        if (args != null) {
            weatherResult = args.getParcelable(WEATHER_MESSAGE);
        }
        if (weatherResult != null) {
            List<String> weekForecast = weatherResult.getWeekForecast();
            //Log.d(TAG, weatherResult.getWeekForecast().toString());
            if (weekForecast != null) {
                forecastRecyclerView.setAdapter(new ForecastRecyclerViewAdapter(weekForecast));
            }
        } else {
            throw new NullPointerException(WEATHER_DETAILS_FRAGMENT_WEATHER_RESULT_IS_NULL);
        }
        titleText.setOnClickListener(new TitleTextOnClickListener());
        return rootView;
    }

    private class TitleTextOnClickListener implements View.OnClickListener {
        @SuppressWarnings("HardCodedStringLiteral")
        private static final String TAG = "TitleTextOnClickListener";
        @SuppressWarnings("HardCodedStringLiteral")
        private static final String ACTIVITY_NULL = "Activity == null";
        @SuppressWarnings("HardCodedStringLiteral")
        private static final String FONTS_TROIKA_OTF = "fonts/troika.otf";

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            Menu menu = popupMenu.getMenu();
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, menu);
            FragmentActivity activity = getActivity();
            if (activity == null) throw new RuntimeException(TAG + ACTIVITY_NULL);
            UtilMethods.changeFontMenu(menu, activity);
            popupMenu.show();
        }
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
                UtilMethods.changeFontTextView(forecastTextView, activity);
                forecastImageView = itemView.findViewById(R.id.forecast_image_view);
            }

            void setForecastImageView(int position) {
                UtilMethods.setWeatherImage(getResources(),
                        forecastImageView, forecastList.get(position));
            }
        }
    }
}
