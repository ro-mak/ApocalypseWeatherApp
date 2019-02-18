package ru.makproductions.apocalypseweatherapp.view.weather.details;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import timber.log.Timber;

public class ApocalypseCountdownFragment extends Fragment {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MUNUTE = 60;
    private static final int DELAY = 1000;
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String DATE_OF_DOOM = "DATE_OF_DOOM";
    private static final long SECONDS_IN_DAY = 3600 * 24;
    private static final long SECONDS_IN_YEAR = 3600 * 365 * 24;
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String COUNTDOWN_FORMAT = "%d %s : %d %s : %d %s : %02d %s :" +
            " %02d %s";
    private long seconds;
    private boolean running;
    private long currentTime;
    private long dateOfDoom;
    private long countDownTime;
    @BindView(R.id.countdown_title_view)
    TextView apocalypseMessage;
    @BindView(R.id.countdown_view)
    TextView apocalypseTimer;

    //Fragment nested in ShowWeatherFragment
    public static ApocalypseCountdownFragment init(Bundle bundle) {
        ApocalypseCountdownFragment countdownFragment = new ApocalypseCountdownFragment();
        if (bundle != null) {
            countdownFragment.setArguments(bundle);
        }
        return countdownFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.countdown_view, container, false);
        ButterKnife.bind(this, rootView);
        loadArgs();
        currentTime = Calendar.getInstance().getTimeInMillis();
        countDownTime = dateOfDoom - currentTime;
        running = true;
        setFonts();
        runCountdown(rootView);
        return rootView;
    }

    private void loadArgs() {
        Bundle args = getArguments();
        if (args != null) dateOfDoom = args.getLong(DATE_OF_DOOM);
        Timber.d(dateOfDoom + " " + args);
        Timber.d(currentTime + " " + args);
    }

    private void setFonts() {
        UtilMethods.changeFontTextView(apocalypseMessage);
        UtilMethods.changeFontTextView(apocalypseTimer);
    }

    @Override
    public void onDetach() {
        running = false;
        super.onDetach();
    }

    private void runCountdown(View rootView) {
        if (running) {
            seconds = countDownTime / 1000;
            final TextView timeView = rootView.findViewById(R.id.countdown_view);
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (running) {
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

                        String time = String.format(Locale.US, COUNTDOWN_FORMAT, years, y, days, d, hours, h,
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
