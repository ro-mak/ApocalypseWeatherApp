package ru.makproductions.apocalypseweatherapp.view.options;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.makproductions.afilechooser.utils.FileUtils;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;

import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_REQUEST_CODE;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.OPTIONS_ACTIVITY_TAG;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);
        Button button = findViewById(R.id.avatar_change_options_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.avatar_change_options_button) {
            Log.d(OPTIONS_ACTIVITY_TAG, "onClick: ");
            try {
                Intent intent = Intent.createChooser(FileUtils.createGetContentIntent(), UtilVariables.SELECT_A_FILE);
                startActivityForResult(intent, UtilVariables.AVATAR_REQUEST_CODE);
                Log.d(OPTIONS_ACTIVITY_TAG, AVATAR);
            } catch (Exception e) {
                Log.e(UtilVariables.OPTIONS_ACTIVITY_TAG, e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(OPTIONS_ACTIVITY_TAG, "OnActivityResult");
        if (requestCode == AVATAR_REQUEST_CODE) {
            //TODO: Change avatar from options
//            Log.d(OPTIONS_ACTIVITY_TAG, "OnActivityResult INN");
//            NavigationView navigationView = findViewById(R.id.nav_view);
//            ImageView avatar;
//            if(navigationView!=null) {
//                avatar = navigationView.getHeaderView(0).findViewById(R.id.nav_avatar);
//                UtilMethods.changeAvatar(resultCode, data, getSharedPreferences(UtilVariables.AVATAR_PREFS, MODE_PRIVATE), this, avatar);
//            }
        }
    }
}
