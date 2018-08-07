package ru.makproductions.apocalypseweatherapp.view.weather_details;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.WeatherResult;
import ru.makproductions.apocalypseweatherapp.presenter.TroikaTypefaceSpan;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

public class WeatherDetailsFragment extends Fragment {

    public static final boolean HAS_FIXED_SIZE_TRUE = true;
    private static final String WEATHER_MESSAGE = "weather_message";
    private static final String TAG = "##WDFragment##";
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_details_fragment, container, false);
        titleText = rootView.findViewById(R.id.details_title);
        FragmentActivity activity = getActivity();
        UtilMethods.changeFontTextView(titleText, activity);
        RecyclerView forecastRecyclerView = (RecyclerView) rootView.findViewById(R.id.forecast_recycler_view);
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
            throw new NullPointerException("WeatherDetailsFragment!!! weatherResult is null");
        }
        titleText.setOnClickListener(new TitleTextOnClickListener());
        return rootView;
    }

    private class TitleTextOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            Menu menu = popupMenu.getMenu();
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, menu);
            Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/troika.otf");
            TroikaTypefaceSpan typefaceSpan = new TroikaTypefaceSpan("", typeFace);
            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                SpannableString title = new SpannableString(menuItem.getTitle());
                title.setSpan(typefaceSpan, 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                menuItem.setTitle(title);
            }
            popupMenu.show();
        }
    }

    private class ForecastRecyclerViewAdapter
            extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {
        private List<String> forecastList;

        public ForecastRecyclerViewAdapter(List<String> forecastList) {
            this.forecastList = forecastList;
        }

        @Override
        public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ForecastViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ForecastViewHolder holder, int position) {
            holder.forecastTextView.setText(forecastList.get(position).replaceAll("_", " "));
            holder.setForecastImageView(position);
        }

        @Override
        public int getItemCount() {
            return forecastList.size();
        }

        class ForecastViewHolder extends RecyclerView.ViewHolder {
            private TextView forecastTextView;
            private ImageView forecastImageView;

            public ForecastViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.forecast_list_item, parent, false));

                forecastTextView = itemView.findViewById(R.id.forecast_textview);
                UtilMethods.changeFontTextView(forecastTextView, getActivity());
                forecastImageView = itemView.findViewById(R.id.forecast_image_view);
            }

            public void setForecastImageView(int position) {
                UtilMethods.setWeatherImage(getResources(),
                        forecastImageView, forecastList.get(position));
            }
        }
    }
}
