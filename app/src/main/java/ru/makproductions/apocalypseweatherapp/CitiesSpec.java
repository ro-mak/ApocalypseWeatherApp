package ru.makproductions.apocalypseweatherapp;




import android.content.*;

class CitiesSpec
 {
    static String getWeatherDescription(Context context, int position, boolean pressure,
                                        boolean tommorowForecast, boolean weekForecast) {
        String[] descriptions = context.getResources().getStringArray(R.array.descriptions);
        String result = descriptions[position];
        if (pressure) {
            result += "\n"
                    + context.getResources().getStringArray(R.array.pressure)[position];
        }
        if (tommorowForecast) {
             result += "\n" + context.getResources().getStringArray(R.array.tomorrow_forecast)[position];
        }
        if(weekForecast){
            result += "\n" + context.getResources().getStringArray(R.array.week_forecast)[position];
        }
        return result;
    }
}
