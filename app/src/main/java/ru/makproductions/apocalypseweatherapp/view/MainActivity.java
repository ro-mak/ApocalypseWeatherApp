package ru.makproductions.apocalypseweatherapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.makproductions.afilechooser.utils.FileUtils;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.WeatherResult;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;
import ru.makproductions.apocalypseweatherapp.view.options.OptionsActivity;
import ru.makproductions.apocalypseweatherapp.view.show.weather.ShowWeatherActivity;
import ru.makproductions.apocalypseweatherapp.view.show.weather.ShowWeatherFragment;
import ru.makproductions.apocalypseweatherapp.view.weather.list.WeatherListListener;

import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_PREFS;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_REQUEST_CODE;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.MAIN_ACTIVITY_TAG;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.ON_ACTIVITY_RESULT;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.PERMISSIONS_REQUEST_CODE;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.SENSOR_SERVICE_IS_NULL;

//main class
public class MainActivity extends AppCompatActivity implements WeatherListListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageView avatar;
    private boolean permissionGranted;
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor humiditySensor;
    private SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEATHER_MESSAGE = "weather_message";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String WEATHER_BUNDLE = "weather_bundle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Log.d(MAIN_ACTIVITY_TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(AVATAR_PREFS, MODE_PRIVATE);
        //add icon to the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            throw new RuntimeException(UtilVariables.MAIN_ACTIVITY_TAG + getString(R.string.actionbar_runtime_exception_warning));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.logo_layout);

        View customView = actionBar.getCustomView();
        //change font of the title on the action bar
        TextView titleView = customView.findViewById(R.id.title);
        UtilMethods.changeFontTextView(titleView, this);

        Toolbar toolbar = findViewById(R.id.toolbar_top);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (sharedPreferences != null) {
                    String avPath = sharedPreferences.getString(AVATAR, null);
                    if (avPath != null) UtilMethods.loadAvatar(avPath, MainActivity.this, avatar);
                }
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        avatar = navigationView.getHeaderView(0).findViewById(R.id.nav_avatar);
        TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.user_name_text_view);
        if (userNameTextView != null) UtilMethods.changeFontTextView(userNameTextView, this);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = Intent.createChooser(FileUtils.createGetContentIntent(), UtilVariables.SELECT_A_FILE);
                    startActivityForResult(intent, UtilVariables.AVATAR_REQUEST_CODE);
                    Log.d(MAIN_ACTIVITY_TAG, AVATAR);
                } catch (Exception e) {
                    Log.e(UtilVariables.MAIN_ACTIVITY_TAG, e.getMessage());
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        UtilMethods.changeFontMenu(menu, this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null)
            throw new RuntimeException(MAIN_ACTIVITY_TAG + SENSOR_SERVICE_IS_NULL);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (temperatureSensor != null)
            sensorManager.registerListener(new SensorListener(), temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (humiditySensor != null)
            sensorManager.registerListener(new SensorListener(), humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private class SensorListener implements SensorEventListener {

        @SuppressWarnings("HardCodedStringLiteral")
        private static final String C = " C°";
        @SuppressWarnings("HardCodedStringLiteral")
        private static final String PERCENT_HUMIDITY = " %";
        private static final String SPACE = " ";

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int type = event.sensor.getType();
            if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                if (values[0] != 0) {
                    TextView textView = findViewById(R.id.current_temp_text_view);
                    String measure = getString(R.string.current_temp_string) + SPACE + values[0] + C;
                    textView.setText(measure);
                    UtilMethods.changeFontTextView(textView, MainActivity.this);
                }
            } else if (type == Sensor.TYPE_RELATIVE_HUMIDITY) {
                if (values[0] != 0) {
                    TextView textView = findViewById(R.id.current_humidity_text_view);
                    String measure = getString(R.string.current_humidity_string) + SPACE + values[0] + PERCENT_HUMIDITY;
                    textView.setText(measure);
                    UtilMethods.changeFontTextView(textView, MainActivity.this);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    protected void onStart() {
        Log.d(MAIN_ACTIVITY_TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(MAIN_ACTIVITY_TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(MAIN_ACTIVITY_TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(MAIN_ACTIVITY_TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.d(MAIN_ACTIVITY_TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(MAIN_ACTIVITY_TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
            }
        }
    }

    //a method previously used in learning purposes to know if sharing was success
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UtilVariables.SUCCESS_CODE) {
            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == AVATAR_REQUEST_CODE) {
            Log.d(MAIN_ACTIVITY_TAG, ON_ACTIVITY_RESULT + "requestCode" + requestCode);
            Log.d(MAIN_ACTIVITY_TAG, ON_ACTIVITY_RESULT + "resultCode" + resultCode);
            UtilMethods.changeAvatar(resultCode, data, sharedPreferences, this, avatar);
        }
    }


    @Override
    public void onListItemClick(WeatherResult result) {
        showWeather(this, result);
    }

    public static void showWeather(FragmentActivity activity, WeatherResult result) {
        View fragmentContainer = activity.findViewById(R.id.fragment_container);
        Bundle bundle = new Bundle();
        bundle.putParcelable(WEATHER_MESSAGE, result);
        //if tablet use first, if not use second
        if (fragmentContainer != null) {
            ShowWeatherFragment showWeatherFragment = ShowWeatherFragment.init(bundle);
            android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, showWeatherFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Intent intent = new Intent(activity, ShowWeatherActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_options) {
            startActivity(new Intent(this, OptionsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_options) {
            startActivity(new Intent(this, OptionsActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
