package ru.makproductions.apocalypseweatherapp;

import android.graphics.*;
import android.support.v4.app.FragmentActivity;
import android.widget.*;

public class UtilMethods
{
	public static void changeFontTextView(TextView view, FragmentActivity activity){
		Typeface font = Typeface.createFromAsset(activity.getAssets(),"fonts/troika.otf");
		view.setTypeface(font);
	}
}
