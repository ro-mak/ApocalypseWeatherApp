package ru.makproductions.apocalypseweatherapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class WeatherLoader {

    public static final String TAG = "WeatherLoader";
    private static final String KEY = "x-api-key" ;
    private static final String OPEN_WEATHER_API_MAP = "api.openweathermap.org/data/2.5/weather?id=%d&units=metric&lang=ru";
    public static final String NEW_LINE = "\n";
    public static final int ALL_GOOD = 200;
    public static final String RESPONSE = "cod";

    static JSONObject getJSONData(Context context, int city) {
        try {
            URL url = new URL(String.format(Locale.ENGLISH,OPEN_WEATHER_API_MAP, city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY,context.getString(R.string.openweather_api_key));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String temp;
            while ((temp = bufferedReader.readLine())!=null){
                rawData.append(temp).append(NEW_LINE);
            }
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if(jsonObject.getInt(RESPONSE) != ALL_GOOD){
                return jsonObject;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return null;
    }
}
