package ru.makproductions.apocalypseweatherapp.view.ui.activities.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.makproductions.afilechooser.utils.FileUtils;
import ru.makproductions.apocalypseweatherapp.App;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.model.image.IImageLoader;
import ru.makproductions.apocalypseweatherapp.presenter.main.MainPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilMethods;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;
import ru.makproductions.apocalypseweatherapp.view.main.MainView;
import ru.makproductions.apocalypseweatherapp.view.sensors.SensorListener;
import ru.makproductions.apocalypseweatherapp.view.ui.activities.options.OptionsActivity;
import timber.log.Timber;

import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_PREFS;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_REQUEST_CODE;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.ON_ACTIVITY_RESULT;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.PERMISSIONS_REQUEST_CODE;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.SENSOR_SERVICE_IS_NULL;

//main class
public class MainActivity extends MvpAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener, MainView {

    private ImageView avatar;
    private boolean permissionGranted;
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor humiditySensor;
    private SharedPreferences sharedPreferences;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @InjectPresenter
    MainPresenter presenter;

    private Toolbar toolbar;
    private SensorListener sensorListener;
    private ActionBarDrawerToggle toggle;
    @Inject
    IImageLoader imageLoader;

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {
        MainPresenter presenter = new MainPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(this);
        Timber.e("presenter created");
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Timber.d("OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadPrefs();
        initActionBar();
        initDrawer();
        initMenu();
        initSensors();
    }

    private void loadPrefs() {
        sharedPreferences = getSharedPreferences(AVATAR_PREFS, MODE_PRIVATE);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            throw new RuntimeException(getString(R.string.actionbar_runtime_exception_warning));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.logo_layout);
        toolbar = findViewById(R.id.toolbar_top);
        View customView = actionBar.getCustomView();
        //change font of the title on the action bar
        TextView titleView = customView.findViewById(R.id.title);
        UtilMethods.changeFontTextView(titleView);
    }

    private void initDrawer() {
        avatar = navigationView.getHeaderView(0).findViewById(R.id.nav_avatar);
        onAvatarClick();
        initToggle();
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.user_name_text_view);
        if (userNameTextView != null) UtilMethods.changeFontTextView(userNameTextView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initMenu() {
        Menu menu = navigationView.getMenu();
        UtilMethods.changeFontMenu(menu);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null)
            throw new RuntimeException(SENSOR_SERVICE_IS_NULL);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (temperatureSensor != null)
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (humiditySensor != null)
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initToggle() {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (sharedPreferences != null) {
                    String avPath = sharedPreferences.getString(AVATAR, null);
                    if (avPath != null) presenter.saveAvatarPath(avPath);
                }
                super.onDrawerOpened(drawerView);
            }
        };
    }

    private void onAvatarClick() {
        avatar.setOnClickListener(v -> {
            try {
                requestReadExternalStoragePermissions();
                Timber.d(AVATAR);
            } catch (Exception e) {
                Timber.e(e);
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorListener == null) sensorListener = new SensorListener();
        sensorListener.onChange(event, this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                openChooser();
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
            Timber.d(ON_ACTIVITY_RESULT + "requestCode" + requestCode);
            Timber.d(ON_ACTIVITY_RESULT + "resultCode" + resultCode);
            if (resultCode == RESULT_OK) {
                final Uri uri = data.getData();
                String path = FileUtils.getPath(this, uri);
                Timber.d("requestReadExternalStoragePermissions: Checking Path");
                if (FileUtils.isLocal(path)) {
                    Timber.d("requestReadExternalStoragePermissions: Loading");
                    sharedPreferences.edit().putString(AVATAR, path).apply();
                    presenter.saveAvatarPath(path);
                }
            } else {
                Timber.d("requestReadExternalStoragePermissions: RESULT NOT OK");
            }
        }
        presenter.loadAvatar();
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

    @Override
    public void loadAvatar(File file) {
        imageLoader.loadInto(avatar, file);
    }

    public void requestReadExternalStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Timber.e("explanation bla bla");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openChooser();
        }
    }

    private void openChooser() {
        Intent intent = Intent.createChooser(FileUtils.createGetContentIntent(), UtilVariables.SELECT_A_FILE);
        startActivityForResult(intent, UtilVariables.AVATAR_REQUEST_CODE);
    }
}
