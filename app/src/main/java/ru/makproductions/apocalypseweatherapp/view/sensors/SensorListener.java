package ru.makproductions.apocalypseweatherapp.view.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;

public class SensorListener {

    @SuppressWarnings("HardCodedStringLiteral")
    private static final String C = " C°";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String PERCENT_HUMIDITY = " %";
    private static final String SPACE = " ";


    public void onChange(SensorEvent event, FragmentActivity activity) {
        float[] values = event.values;
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            if (values[0] != 0) {
                TextView textView = activity.findViewById(R.id.current_temp_text_view);
                String measure = activity.getString(R.string.current_temp_string) + SPACE + values[0] + C;
                textView.setText(measure);
                UtilMethods.changeFontTextView(textView);
            }
        } else if (type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            if (values[0] != 0) {
                TextView textView = activity.findViewById(R.id.current_humidity_text_view);
                String measure = activity.getString(R.string.current_humidity_string) + SPACE + values[0] + PERCENT_HUMIDITY;
                textView.setText(measure);
                UtilMethods.changeFontTextView(textView);
            }
        }
    }
}