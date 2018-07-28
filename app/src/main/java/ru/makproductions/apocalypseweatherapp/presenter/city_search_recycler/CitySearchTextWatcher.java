package ru.makproductions.apocalypseweatherapp.presenter.city_search_recycler;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import ru.makproductions.apocalypseweatherapp.presenter.city_search_recycler.CitySearchRecyclerAdapter;

public class CitySearchTextWatcher implements TextWatcher {
    private static final String CITY_SEARCH_TEXT_WATCHER = "CitySearchTextWatcher";
    private CitySearchRecyclerAdapter adapter;
    private int position;
    public CitySearchTextWatcher(CitySearchRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.position = adapter.getTownSelected();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.sendToTop(position);
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Log.d(CITY_SEARCH_TEXT_WATCHER, "NotifyDataSetChanged:" + i);
            adapter.notifyItemChanged(i);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
