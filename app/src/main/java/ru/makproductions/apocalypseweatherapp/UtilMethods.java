package ru.makproductions.apocalypseweatherapp;
import android.app.*;
import android.view.*;
import android.graphics.*;
import android.widget.*;

public class UtilMethods
{
	public static void changeFontTextView(TextView view, Activity activity){
		Typeface font = Typeface.createFromAsset(activity.getAssets(),"fonts/troika.otf");
		view.setTypeface(font);
	}
}
