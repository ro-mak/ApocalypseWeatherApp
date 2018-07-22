package ru.makproductions.apocalypseweatherapp;

import android.content.*;
import android.os.*;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.*;

import android.widget.*;

//main class
public class MainActivity extends AppCompatActivity implements WeatherListListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HeyHOO###############";
    private final int SUCCESS_CODE = 666;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    private static final String WEATHER_MESSAGE = "weather_message";

    private static final String WEATHER_BUNDLE = "weather_bundle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //add icon to the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.logo_layout);
        View customView = actionBar.getCustomView();
        //change font of the title on the action bar
        TextView titleView = (TextView) customView.findViewById(R.id.title);
        UtilMethods.changeFontTextView(titleView, this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,R.string.app_name,R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    //a method previously used in learning purposes to know if sharing was success
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUCCESS_CODE) {
            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {
            }
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
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
