package ru.makproductions.apocalypseweatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class ApocalypseCountdownFragment extends Fragment {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MUNUTE = 60;
    private static final int DELAY = 1000;
    private static final String KEY_SECONDS = "seconds";
    private static final String KEY_RUNNING = "running";
    private static final String KEY_WAS_RUNNING = "wasRunning";
    private static final String DATE_OF_DOOM = "DATE_OF_DOOM";
    private static final long SECONDS_IN_DAY = 3600 * 24;
    private static final long SECONDS_IN_YEAR = 3600 * 365 * 24;
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
        Log.d("CurrentTime", currentTime + " " + args);
        running = true;
        TextView apocalypseMessage = rootView.findViewById(R.id.countdown_title_view);
        TextView apocalypseTimer = rootView.findViewById(R.id.countdown_view);
        FragmentActivity activity = getActivity();
        UtilMethods.changeFontTextView(apocalypseMessage,activity);
        UtilMethods.changeFontTextView(apocalypseTimer,activity);
        runCountdown(rootView);
        return rootView;
    }

    @Override
    public void onDetach() {
        running = false;
        super.onDetach();
    }

    private void runCountdown(View rootView) {
        if(running) {
            seconds = countDownTime / 1000;
            final TextView timeView = (TextView)
                    rootView.findViewById(R.id.countdown_view);
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(running) {
                        long years = seconds / SECONDS_IN_YEAR;
                        long days = (seconds % SECONDS_IN_YEAR) / SECONDS_IN_DAY;
                        long hours = (seconds % SECONDS_IN_DAY) / SECONDS_IN_HOUR;
                        long minutes = (seconds % SECONDS_IN_HOUR) / SECONDS_IN_MUNUTE;
                        long secs = seconds % SECONDS_IN_MUNUTE;
                        String y = getResources().getString(R.string.year);
                        String d = getResources().getString(R.string.day);
                        String h = getResources().getString(R.string.hour);
                        String m = getResources().getString(R.string.minute);
                        String s = getResources().getString(R.string.second);

                        String time = String.format(Locale.US, "%d %s %d %s %d %s %02d %s %02d %s", years, y, days, d, hours, h,
                                minutes, m, secs, s);
                        timeView.setText(time);
                        seconds--;
                        handler.postDelayed(this, DELAY);
                    }
                }
            });
        }
    }
}
