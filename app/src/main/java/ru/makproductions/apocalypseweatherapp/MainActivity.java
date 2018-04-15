package ru.makproductions.apocalypseweatherapp;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.util.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.support.v4.app.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity implements WeatherListListener
{

    private static final String TAG = "HeyHOO###############";
    private final int SUCCESS_CODE = 666;
	private static final String WEATHER_MESSAGE = "weather_message";

	private static final String WEATHER_BUNDLE ="weather_bundle";


    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(R.style.AppTheme);
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.logo_layout);
		View customView = actionBar.getCustomView();
		TextView titleView = (TextView) customView.findViewById(R.id.title);
		UtilMethods.changeFontTextView(titleView, this);
    }

    @Override
    protected void onStart()
	{
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop()
	{
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy()
	{
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onResume()
	{
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart()
	{
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause()
	{
        Log.d(TAG, "onPause");
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        if (requestCode == SUCCESS_CODE)
		{
            if (resultCode == RESULT_OK)
			{
            }
			else if (resultCode == RESULT_CANCELED)
			{
            }
        }
    }

    @Override
    public void onListItemClick(String result)
	{
		showWeather(this, result);
    }
	public static void showWeather(FragmentActivity activity, String result)
	{
		View fragmentContainer = activity.findViewById(R.id.fragment_container);
		if (fragmentContainer != null)
		{
			ShowWeatherFragment showWeatherFragment = new ShowWeatherFragment();

			showWeatherFragment.setWeather(result);
			android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment_container, showWeatherFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		else
		{
			Intent intent = new Intent(activity, ShowWeatherActivity.class);
			Bundle bundle = new Bundle(); 
			bundle.putString(WEATHER_MESSAGE, result);
			intent.putExtra(WEATHER_BUNDLE, bundle);
			activity.startActivity(intent);
		}
	}
}
