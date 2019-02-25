package ru.makproductions.apocalypseweatherapp.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.presenter.TroikaTypefaceSpan;
import timber.log.Timber;

public class UtilMethods {
    private static final String FONTS_TROIKA_OTF = App.getInstance().getString(R.string.fonts_file);
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TRANSLITERATE_FROM_RUSSIAN_TO_ENGLISH = "transliterateFromRussianToEnglish: ";

    public static void changeFontTextView(TextView view) {
        Typeface font = Typeface.createFromAsset(App.getInstance().getAssets(), FONTS_TROIKA_OTF);
        view.setTypeface(font);
    }

    public static void changeFontMenu(Menu menu) {
        Typeface typeFace = Typeface.createFromAsset(App.getInstance().getAssets(), FONTS_TROIKA_OTF);
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
        Timber.e("%s%s", TRANSLITERATE_FROM_RUSSIAN_TO_ENGLISH, result.toString());
        return result.toString();
    }
}
