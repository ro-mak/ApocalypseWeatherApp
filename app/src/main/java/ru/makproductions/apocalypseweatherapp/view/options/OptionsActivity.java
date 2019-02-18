package ru.makproductions.apocalypseweatherapp.view.options;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.makproductions.afilechooser.utils.FileUtils;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;
import timber.log.Timber;

import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_REQUEST_CODE;

public class OptionsActivity extends AppCompatActivity {

    @BindView(R.id.avatar_change_options_button)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.avatar_change_options_button)
    public void onChangeAvatarButtonClick(View v) {
        Timber.d("onClick: ");
        try {
            Intent intent = Intent.createChooser(FileUtils.createGetContentIntent(), UtilVariables.SELECT_A_FILE);
            startActivityForResult(intent, UtilVariables.AVATAR_REQUEST_CODE);
            Timber.d(AVATAR);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("OnActivityResult");
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
