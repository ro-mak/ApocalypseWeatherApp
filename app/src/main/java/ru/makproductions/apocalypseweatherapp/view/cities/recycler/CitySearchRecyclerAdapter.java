package ru.makproductions.apocalypseweatherapp.view.cities.recycler;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.cities.CitiesHandler;
import ru.makproductions.apocalypseweatherapp.presenter.main.ICityListPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import timber.log.Timber;

public class CitySearchRecyclerAdapter extends RecyclerView.Adapter<CitySearchRecyclerAdapter.MyViewHolder> implements Filterable {
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String NAME = "Name: ";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String DRAWABLE_TYPE = "drawable";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String NEW_LINE = "/n";
    private static final String SPACE = " ";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String GET_TOWN_SELECTED_TO_SHOW = "getTownSelectedToShow: ";
    private List<String> cities;
    private List<String> citiesFiltered;
    private List<String> citiesToShow;
    private List<String> citiesToShowFiltered;
    private Map<Integer, String> showFilteredMap;
    private EditText citySearchEditText;
    private int townSelected;
    private String cityToShow;
    private Resources resources;
    private CitiesHandler citiesHandler;
    private ICityListPresenter presenter;

    public CitySearchRecyclerAdapter(EditText citySearchEditText, ICityListPresenter presenter) {
        resources = App.getInstance().getResources();
        citiesHandler = new CitiesHandler(resources);
        this.cities = citiesHandler.getCities();
        this.citiesToShow = citiesHandler.getCitiesInEnglish();
        this.citySearchEditText = citySearchEditText;
        this.citiesFiltered = cities;
        this.citiesToShowFiltered = citiesToShow;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public CitySearchRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CitySearchRecyclerAdapter.MyViewHolder(inflater, parent);
    }

    private int imageId;

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Override
    public void onBindViewHolder(@NonNull CitySearchRecyclerAdapter.MyViewHolder holder, int position) {
        holder.position = position;
        presenter.bindView(holder);
        try {
            String cityName = citiesFiltered.get(position);
            //Gets a city name in English casts to lower case and transforms to resource syntax
            cityToShow = citiesToShowFiltered.get(position).toLowerCase();
            cityToShow = UtilMethods.formatCityName(cityToShow);
            Timber.d("%s%s", NAME, cityToShow);
            holder.city.setText(cityName);
            try {
                imageId = resources.getIdentifier(cityToShow, DRAWABLE_TYPE, App.getInstance().getPackageName());
            } catch (NullPointerException e) {

                Timber.d(e.getMessage() + NEW_LINE + SPACE + NAME + cityToShow);
            }
            holder.cityImage.setImageResource(imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return citiesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                try {
                    String charSequence = constraint.toString();
                    String lineTyped = citySearchEditText.getText().toString().toLowerCase();
                    if (charSequence.isEmpty()) {
                        citiesFiltered = cities;
                        citiesToShowFiltered = citiesToShow;
                    } else {
                        List<String> filteredList = new ArrayList<>();
                        List<String> newShowList = new ArrayList<>();
                        showFilteredMap = new TreeMap<>();
                        for (int i = 0; i < cities.size(); i++) {
                            String city = cities.get(i);
                            if (city.toLowerCase().startsWith(lineTyped)) {
                                filteredList.add(city);
                                showFilteredMap.put(i, citiesToShow.get(i));
                            }
                        }

                        citiesFiltered = filteredList;
                        for (Map.Entry<Integer, String> entry : showFilteredMap.entrySet()) {
                            newShowList.add(entry.getValue());
                        }
                        citiesToShowFiltered = newShowList;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = citiesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                try {
                    //noinspection unchecked
                    citiesFiltered = Arrays.asList(convertResultToString(results.values).toString().replaceAll("[\\[\\]]", "").split(","));
                } catch (Exception e) {
                    e.printStackTrace();
                    Timber.e(results.values.toString());
                }
                notifyDataSetChanged();
            }
        };
    }

    private void showDescription() {
        presenter.loadWeather(getTownSelectedToShow(), citiesHandler, App.getInstance().getResources().getConfiguration().locale);
    }

    private int getTownSelectedToShow() {
        int result = 0;
        if (!citiesToShow.equals(citiesToShowFiltered)) {
            for (Map.Entry<Integer, String> entry : showFilteredMap.entrySet()) {
                if (entry.getValue().equals(citiesToShowFiltered.get(townSelected))) {
                    result = entry.getKey();
                    Timber.d("%s%s", GET_TOWN_SELECTED_TO_SHOW, result);
                }
            }
        } else {
            return townSelected;
        }
        return result;
    }


    private void sendToTop(int index) {
        citiesHandler.sendToTop(index);
        cities = citiesHandler.getCities();
        citiesFiltered = cities;
        citiesToShow = citiesHandler.getCitiesInEnglish();
        citiesToShowFiltered = citiesToShow;
    }

    public class MyViewHolder extends CitySearchViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, CityListItemView {
        private TextView city;
        private ImageView cityImage;
        private final MenuItem.OnMenuItemClickListener onContextMenuClick = item -> {
            if (item.getItemId() == R.id.show_on_top_item) {
                sendToTop(getAdapterPosition());
                notifyDataSetChanged();
                return true;
            }
            return false;
        };

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.city_list_item, parent, false));
            cityImage = itemView.findViewById(R.id.city_image_view);
            city = itemView.findViewById(R.id.city);
            UtilMethods.changeFontTextView(city);
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

        private int position;

        @Override
        public void setName(String name) {

        }

        @Override
        public int getPos() {
            return position;
        }
    }
}
