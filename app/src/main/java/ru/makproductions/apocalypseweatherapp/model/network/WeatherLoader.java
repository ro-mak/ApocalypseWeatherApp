package ru.makproductions.apocalypseweatherapp.model.network;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import ru.makproductions.apocalypseweatherapp.R;

public class WeatherLoader extends AsyncTask<Context, Integer, JSONObject> {

    private static final String TAG = "WeatherLoader";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;
    private static final String RESPONSE = "cod";
    private static final String GET_JSONDATA = "getJSONData: ";
    private static final String REQUEST_METHOD_GET = "GET";
    private String city;

    public WeatherLoader(String city) {
        this.city = city;
    }

    @Override
    protected JSONObject doInBackground(Context... context) {
        Resources resources = context[0].getResources();
        try {
            URL url = new URL(String.format(Locale.ENGLISH, resources.getString(R.string.open_weather_map_api_url), city, resources.getString(R.string.openweather_api_key)));
            Log.d(TAG, GET_JSONDATA + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD_GET);
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
            Log.e(TAG, GET_JSONDATA + jsonObject.toString());
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
