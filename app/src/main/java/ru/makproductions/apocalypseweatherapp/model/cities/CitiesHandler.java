package ru.makproductions.apocalypseweatherapp.model.cities;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

public class CitiesHandler {
    private List<String> citiesList;
    private List<String> citiesInEnglishList;
    private List<String> citiesToFind;

    private static CitiesHandler instance;

    private CitiesHandler(Resources resources) {
        citiesList = Arrays.asList(resources.getStringArray(R.array.cities));
        citiesToFind = Arrays.asList(resources.getStringArray(R.array.cities_to_find));
        citiesInEnglishList = UtilMethods.getEnglishCitiesNamesList(resources);
    }

    public static CitiesHandler getInstance() {
        if (instance == null) {
            instance = new CitiesHandler(App.getInstance().getResources());
        }
        return instance;
    }

    public List<String> getCities() {
        return citiesList;
    }

    public List<String> getCitiesInEnglish() {
        return citiesInEnglishList;
    }

    public List<String> getCitiesToFind() {
        return citiesToFind;
    }

    public void sendToTop(int index) {
        List<String> newCitiesList = new ArrayList<>();
        List<String> newCitiesInEnglishList = new ArrayList<>();
        List<String> newCitiesToFindList = new ArrayList<>();
        String topCity = citiesList.get(index);
        String topCityToShow = citiesInEnglishList.get(index);
        String topCityToFind = citiesToFind.get(index);
        newCitiesList.add(topCity);
        newCitiesInEnglishList.add(topCityToShow);
        newCitiesToFindList.add(topCityToFind);
        for (String line : citiesList) {
            if (!line.equals(topCity)) newCitiesList.add(line);
        }
        for (String line : citiesInEnglishList) {
            if (!line.equals(topCityToShow)) newCitiesInEnglishList.add(line);
        }
        for (String line : citiesToFind) {
            if (!line.equals(topCityToFind)) newCitiesToFindList.add(line);
        }
        citiesList = newCitiesList;
        citiesInEnglishList = newCitiesInEnglishList;
        citiesToFind = newCitiesToFindList;
    }
}
