package ru.makproductions.apocalypseweatherapp.presenter.city_search_recycler;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.WeatherResult;
import ru.makproductions.apocalypseweatherapp.presenter.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.view.weather_list.WeatherListListener;


public class CitySearchRecyclerAdapter extends RecyclerView.Adapter<CitySearchRecyclerAdapter.MyViewHolder> {
    private final static String TAG = "CitySearchRecAd";
    private static final String NAME = "Name: ";
    private static final String MIPMAP_TYPE = "mipmap";
    private static final String NEW_LINE = "/n";
    private static final String SPACE = " ";
    private List<String> cities;
    private List<String> citiesToShow;
    private EditText citySearchEditText;
    private FragmentActivity activity;
    private int townSelected;
    private WeatherResult result;
    private boolean pressure;
    private boolean tommorowForecast;
    private boolean weekForecast;
    private WeatherListListener weatherListListener;
    private Resources resources;
    private CitiesHandler citiesHandler;
    private boolean cityIsShown = false;

    public CitySearchRecyclerAdapter(FragmentActivity activity, EditText citySearchEditText, WeatherListListener weatherListListener) {
        this.activity = activity;
        resources = activity.getResources();
        citiesHandler = new CitiesHandler(resources);
        this.cities = citiesHandler.getCities();
        this.citiesToShow = citiesHandler.getCitiesInEnglish();
        this.citySearchEditText = citySearchEditText;
        this.weatherListListener = weatherListListener;
    }

    @Override
    public CitySearchRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CitySearchRecyclerAdapter.MyViewHolder(inflater, parent);
    }

    private int imageId;

    @Override
    public void onBindViewHolder(CitySearchRecyclerAdapter.MyViewHolder holder, int position) {
        String cityName = cities.get(position);
        //Gets a city name in English casts to lower case and transforms to resource syntax
        String cityToShow = citiesToShow.get(position).toLowerCase();
        cityIsShown = cityName.toLowerCase().startsWith(citySearchEditText.getText().toString().toLowerCase());
        if (cityIsShown) {
            Log.d(TAG, "City:" + cityName + " EditText:" + citySearchEditText.getText());
            cityToShow = UtilMethods.formatCityName(cityToShow);
            Log.d(TAG, NAME + cityToShow);

            holder.city.setText(cityName);
            try {
                imageId = resources.getIdentifier(cityToShow, MIPMAP_TYPE, activity.getPackageName());
            } catch (NullPointerException e) {
                Log.d(TAG, e.getMessage() + NEW_LINE + SPACE + NAME + cityToShow);
            }
            holder.cityImage.setImageResource(imageId);
        }
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class MyViewHolder extends CitySearchViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView city;
        private ImageView cityImage;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.city_list_item, parent, false));
            cityImage = itemView.findViewById(R.id.city_image_view);
            city = itemView.findViewById(R.id.city);
            UtilMethods.changeFontTextView(city, activity);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            townSelected = getAdapterPosition();
            showDescription();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem showOnTop = menu.add(Menu.NONE, R.id.show_on_top_item, Menu.NONE, R.string.show_on_top);
            showOnTop.setOnMenuItemClickListener(onContextMenuClick);
        }

        private final MenuItem.OnMenuItemClickListener onContextMenuClick = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.show_on_top_item) {
                    sendToTop(getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        };
    }

    private void showDescription() {
        result = WeatherResult.getWeatherDescription(activity, townSelected, pressure, tommorowForecast, weekForecast, citiesHandler);
        weatherListListener.onListItemClick(result);
    }

    public void sendToTop(int index) {
        citiesHandler.sendToTop(index);
        cities = citiesHandler.getCities();
        citiesToShow = citiesHandler.getCitiesInEnglish();
    }

    public int getTownSelected() {
        return townSelected;
    }
}
