package com.bombon.voxr.activity;

import android.content.Intent;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vaughn on 9/5/17.
 */

public class LoginActivity extends BaseActivity{
    private final String TAG = this.getClass().getSimpleName();

    @OnClick(R.id.btn_login)
    void loginOnClick(){
        login();
    }

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
    }
}
