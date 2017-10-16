package com.bombon.voxr.activity;

import android.content.Intent;
<<<<<<< HEAD
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bombon.voxr.R;
import com.bombon.voxr.fragment.AboutFragment;
import com.bombon.voxr.fragment.HistoryFragment;
import com.bombon.voxr.fragment.MainFragment;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchAnimListener;
import com.mahfa.dnswitch.DayNightSwitchListener;

import butterknife.BindView;
=======
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bombon.voxr.R;
import com.bombon.voxr.model.User;
import com.bombon.voxr.service.ServiceCallback;
import com.bombon.voxr.service.UserService;

import javax.inject.Inject;

>>>>>>> abc5ca272fb60f8cf32fae78cfe8385dab5d69aa
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
<<<<<<< HEAD
 * Created by Vaughn on 9/5/17.
 */

public class LoginActivity extends BaseActivity{
    private final String TAG = this.getClass().getSimpleName();
=======
 * Created by Vaughn on 8/14/17.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    UserService userService;
>>>>>>> abc5ca272fb60f8cf32fae78cfe8385dab5d69aa

    @OnClick(R.id.btn_login)
    void loginOnClick(){
        login();
    }

<<<<<<< HEAD
    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.btn_register)
    Button btnRegister;

    @BindView(R.id.username)
    EditText etUsername;

    @BindView(R.id.password)
    EditText etPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
=======
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
>>>>>>> abc5ca272fb60f8cf32fae78cfe8385dab5d69aa
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

<<<<<<< HEAD
    }

    private void login(){
        if (!isValid())
            return;

        // Valid text fields
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private boolean isValid(){
        if (etUsername.getText().toString().isEmpty()) {
            etUsername.setError(getString(R.string.error_field_required));
            etUsername.requestFocus();
            return false;
        }
        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.error_field_required));
            etPassword.requestFocus();
            return false;
        }
        return true;
=======
        // Dagger init
        getComponent().inject(this);

        // Stuffs
        if (userService.isLoggedIn())
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void login() {
        userService.login(new User("bon", "bon"), new ServiceCallback<User>() {
            @Override
            public void getResult(int statusCode, User result) {
                if (statusCode == 200){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
>>>>>>> abc5ca272fb60f8cf32fae78cfe8385dab5d69aa
    }
}
