package ru.makproductions.apocalypseweatherapp.view.ui.activities.options;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.makproductions.afilechooser.utils.FileUtils;
import ru.makproductions.apocalypseweatherapp.R;
import ru.makproductions.apocalypseweatherapp.presenter.options.OptionsPresenter;
import ru.makproductions.apocalypseweatherapp.util.UtilVariables;
import ru.makproductions.apocalypseweatherapp.view.options.OptionsView;
import timber.log.Timber;

import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR;
import static ru.makproductions.apocalypseweatherapp.util.UtilVariables.AVATAR_REQUEST_CODE;

public class OptionsActivity extends MvpAppCompatActivity implements OptionsView {

    @InjectPresenter
    OptionsPresenter presenter;

    @ProvidePresenter
    public OptionsPresenter provideOptionsPresenter() {
        OptionsPresenter presenter = new OptionsPresenter(AndroidSchedulers.mainThread());
        Timber.e("presenter created");
        return presenter;
    }

    @BindView(R.id.avatar_change_options_button)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.e("OnCreate");
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
            presenter.changeAvatar();
        }
    }

    @Override
    public void changeAvatar() {
        //            Timber.d("OnActivityResult INN");
//            NavigationView navigationView = findViewById(R.id.nav_view);
//            ImageView avatar;
//            if(navigationView!=null) {
//                avatar = navigationView.getHeaderView(0).findViewById(R.id.nav_avatar);
//                UtilMethods.requestReadExternalStoragePermissions(resultCode, data, getSharedPreferences(UtilVariables.AVATAR_PREFS, MODE_PRIVATE), this, avatar);
//            }
    }
}
