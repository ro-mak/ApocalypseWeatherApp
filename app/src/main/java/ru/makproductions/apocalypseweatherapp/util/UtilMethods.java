package ru.makproductions.apocalypseweatherapp.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ru.makproductions.apocalypseweatherapp.R;

public class UtilMethods {

    public static final String TAG = "UTIL";

    public static void changeFontTextView(TextView view, FragmentActivity activity) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/troika.otf");
        view.setTypeface(font);
    }

    //Get English names to make finding images easier
    public static List<String> getEnglishCitiesNamesList(Resources resources) {
        Configuration conf = resources.getConfiguration();
        Locale savedLocale = conf.locale;
        conf.locale = Locale.ENGLISH; // whatever you want here
        resources.updateConfiguration(conf, null); // second arg null means don't change
        // retrieve resources from desired locale
        String[] str = resources.getStringArray(R.array.cities);
        // restore original locale
        conf.locale = savedLocale;
        resources.updateConfiguration(conf, null);
        return Arrays.asList(str);
    }

    //So as to find an image by city name
    public static String formatCityName(String name) {
        char[] charArray = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (charArray[i] == '-') {
                charArray[i] = '_';
            }
        }
        return new String(charArray);
    }

    private static String[] alphabet = {"a", "b", "v", "g", "d", "e", "yo", "zh", "z", "i", "y", "k",
            "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch",
            "sh", "sh", "", "y", "", "e", "ju", "ja"};
    private static char[] russianAlphabet = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з',
            'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
            'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};

    public static String transliterateFromRussianToEnglish(String line) {
        char[] letters = line.toLowerCase().toCharArray();
        StringBuilder result = new StringBuilder();
        boolean added = false;
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < russianAlphabet.length; j++) {
                if (letters[i] == russianAlphabet[j]) {
                    result.append(alphabet[j]);
                    added = true;
                    break;
                }
            }
            //if there is a non alphabetical sign add it to the word
            if(!added)result.append(" ");
            added = false;
        }
        if(result.toString().isEmpty()){
            result.append(line);
        }
        Log.e(TAG, "transliterateFromRussianToEnglish: " + result.toString() );
        return result.toString();
    }

    public static void setWeatherImage(Resources resources, ImageView weatherImage, String weather_message) {
        String[] parsedMessage = weather_message.split(" ");
        String tempString = "";
        for (int i = 0; i < parsedMessage.length; i++) {
            tempString = parsedMessage[i];
            if (tempString.contains("clear")) {
                weatherImage.setImageResource(R.mipmap.sunny);
            } else if (tempString.contains("cloud")) {
                weatherImage.setImageResource(R.mipmap.cloudy);
            } else if (tempString.contains("rain")) {
                weatherImage.setImageResource(R.mipmap.raining);
            } else if (tempString.contains("snow")&&!tempString.contains("rain")) {
                weatherImage.setImageResource(R.mipmap.snowing);
            } else if (tempString.contains("rain")&&tempString.contains("snow")) {
                weatherImage.setImageResource(R.mipmap.rain_with_snow);
            } else if (tempString.contains(resources.getString(R.string.weather_type_rainstorm))) {
                weatherImage.setImageResource(R.mipmap.rainstorm);
            } else if (tempString.contains(resources.getString(R.string.weather_type_snowstorm))) {
                weatherImage.setImageResource(R.mipmap.snowstorm);
            }
        }
        weatherImage.setMinimumHeight(192);
        weatherImage.setMinimumWidth(192);

    }
}
