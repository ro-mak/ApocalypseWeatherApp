package ru.makproductions.apocalypseweatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ApocalypseCountdownFragment extends Fragment {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MUNUTE = 60;
    private static final int DELAY = 1000;
    private static final String KEY_SECONDS = "seconds";
    private static final String KEY_RUNNING = "running";
    private static final String KEY_WAS_RUNNING = "wasRunning";
    private static final String DATE_OF_DOOM = "DATE_OF_DOOM";
    private long seconds;
    private boolean running;
    private boolean wasRunning;
    private long currentTime;
    private long dateOfDoom;
    private long countDownTime;

    public static ApocalypseCountdownFragment init(Bundle bundle) {
        ApocalypseCountdownFragment countdownFragment = new ApocalypseCountdownFragment();
        if (bundle != null) {
            countdownFragment.setArguments(bundle);
        }
        return countdownFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.countdown_view, container, false);
        Bundle args = getArguments();
        if(args != null)dateOfDoom = args.getLong(DATE_OF_DOOM);
        currentTime = Calendar.getInstance().getTimeInMillis();
        countDownTime = dateOfDoom - currentTime;
        Log.d("DateOfDoom", dateOfDoom + " " + args);
        runCountdown(rootView);
        return rootView;
    }

    private void runCountdown(View rootView) {
        seconds = countDownTime;
        final TextView timeView = (TextView)
                rootView.findViewById(R.id.countdown_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long hours = seconds / SECONDS_IN_HOUR;
                long minutes = (seconds % SECONDS_IN_HOUR) / SECONDS_IN_MUNUTE;
                long secs = seconds % SECONDS_IN_MUNUTE;
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
