package ru.makproductions.apocalypseweatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class ApocalypseCountdown extends Fragment {
    private static final int MILLS_IN_SECOND = 3600;
    private static final int SECONDS_IN_MUNUTE = 60;
    private static final int DELAY = 1000;
    private static final String KEY_SECONDS = "seconds";
    private static final String KEY_RUNNING = "running";
    private static final String KEY_WAS_RUNNING = "wasRunning";
    private int seconds;
    private boolean running;
    private boolean wasRunning;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void runCountdown(View rootView, long dateOfDoom) {
        final TextView timeView = (TextView)
                rootView.findViewById(R.id.countdown_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / MILLS_IN_SECOND;
                int minutes = (seconds % MILLS_IN_SECOND) / SECONDS_IN_MUNUTE;
                int secs = seconds % 60;
                String time = String.format(Locale.US, "%d:%02d:%02d", hours,
                        minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds--;
                }
                handler.postDelayed(this, DELAY);
            }
        });
    }
}
