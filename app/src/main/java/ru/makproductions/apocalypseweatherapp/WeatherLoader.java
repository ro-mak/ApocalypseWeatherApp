package ru.makproductions.apocalypseweatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

public class WeatherLoader extends AsyncTask<Context, Integer, JSONObject> {

    private static final String TAG = "WeatherLoader";
    private static final String OPEN_WEATHER_API_MAP = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;
    private static final String RESPONSE = "cod";
    private String city;

    public WeatherLoader(String city) {
        this.city = city;
    }

    @Override
    protected JSONObject doInBackground(Context... context) {
        try {
            URL url = new URL(String.format(Locale.ENGLISH, OPEN_WEATHER_API_MAP, city, context[0].getResources().getString(R.string.openweather_api_key)));
            Log.d(TAG, "getJSONData: " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                rawData.append(temp).append(NEW_LINE);
            }
            bufferedReader.close();
            connection.disconnect();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            Log.e(TAG, "getJSONData: " + jsonObject.toString());
            if (jsonObject.getInt(RESPONSE) == ALL_GOOD) {
                return jsonObject;
            }
        } catch (Exception e) {
            Log.e(TAG, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
