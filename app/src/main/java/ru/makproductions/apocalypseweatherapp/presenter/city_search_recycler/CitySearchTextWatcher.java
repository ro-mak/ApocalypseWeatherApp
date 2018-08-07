package ru.makproductions.apocalypseweatherapp.presenter.city_search_recycler;

import android.text.Editable;
import android.text.TextWatcher;

public class CitySearchTextWatcher implements TextWatcher {
    private static final String TAG = "CitySearchTextWatcher";
    private static final String CITY_WATCHER = "CITY_WATCHER";
    private CitySearchRecyclerAdapter adapter;

    public CitySearchTextWatcher(CitySearchRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
