package ru.makproductions.apocalypseweatherapp.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.presenter.TroikaTypefaceSpan;
import timber.log.Timber;

public class UtilMethods {
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TAG = "UTIL";
    private static final int MIN_HEIGHT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 300 : 492;
    private static final int MIN_WIDTH = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 300 : 492;
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String FONTS_TROIKA_OTF = "fonts/troika.otf";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TRANSLITERATE_FROM_RUSSIAN_TO_ENGLISH = "transliterateFromRussianToEnglish: ";

    public static void changeFontTextView(TextView view, FragmentActivity activity) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), FONTS_TROIKA_OTF);
        view.setTypeface(font);
    }

    public static void changeFontMenu(Menu menu, FragmentActivity activity) {
        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(), FONTS_TROIKA_OTF);
        TroikaTypefaceSpan typefaceSpan = new TroikaTypefaceSpan("", typeFace);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString title = new SpannableString(menuItem.getTitle());
            title.setSpan(typefaceSpan, 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(title);
        }
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

    @SuppressWarnings("HardCodedStringLiteral")
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
            if (!added) result.append(" ");
            added = false;
        }
        if (result.toString().isEmpty()) {
            result.append(line);
        }
        Timber.e(TRANSLITERATE_FROM_RUSSIAN_TO_ENGLISH + result.toString());
        return result.toString();
    }

    public static void setWeatherImage(Resources resources, ImageView weatherImage, String weather_message) {
        String[] parsedMessage = weather_message.split(" ");
        String tempString;
        boolean rain = false;
        boolean snow = false;
        boolean clearSky = false;
        boolean clouds = false;
        boolean brokenClouds = false;
        boolean thunderStorm = false;
        boolean fog = false;
        for (int i = 0; i < parsedMessage.length; i++) {
            tempString = parsedMessage[i].toLowerCase();
            if (tempString.contains(resources.getString(R.string.clear_sky))) {
                clearSky = true;
            }
            if (tempString.contains(resources.getString(R.string.rain))) {
                rain = true;
            }
            if (tempString.contains(resources.getString(R.string.snow))) {
                snow = true;
            }
            if (tempString.contains(resources.getString(R.string.broken_clouds))) {
                brokenClouds = true;
            }
            if (tempString.contains(resources.getString(R.string.clouds)) && !brokenClouds) {
                clouds = true;
            }
            if (tempString.contains(resources.getString(R.string.thunder_storm))) {
                thunderStorm = true;
            }
            if (tempString.contains(resources.getString((R.string.fog)))) {
                fog = true;
            }

            if (clearSky) {
                Glide.with(weatherImage).load(R.drawable.sunny).into(weatherImage);
            } else if (brokenClouds) {
                Glide.with(weatherImage).load(R.drawable.broken_clouds).into(weatherImage);
            } else if (clouds) {
                Glide.with(weatherImage).load(R.drawable.cloudy).into(weatherImage);
            } else if (rain && !snow) {
                Glide.with(weatherImage).load(R.drawable.raining).into(weatherImage);
            } else if (snow && !rain) {
                Glide.with(weatherImage).load(R.drawable.snowing).into(weatherImage);
            } else if (rain && snow) {
                Glide.with(weatherImage).load(R.drawable.rain_with_snow).into(weatherImage);
            } else if (thunderStorm) {
                Glide.with(weatherImage).load(R.drawable.rainstorm).into(weatherImage);
            } else if (fog) {

            }

//            } else if (tempString.contains(resources.getString(R.string.weather_type_rainstorm))) {
//                weatherImage.setImageResource(R.mipmap.rainstorm);
//            } else if (tempString.contains(resources.getString(R.string.weather_type_snowstorm))) {
//                weatherImage.setImageResource(R.mipmap.snowstorm);
//            }
        }
        weatherImage.setMinimumHeight(MIN_HEIGHT);
        weatherImage.setMinimumWidth(MIN_WIDTH);

    }
}
