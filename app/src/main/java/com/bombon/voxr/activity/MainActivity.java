package com.bombon.voxr.activity;

import android.Manifest;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.bombon.voxr.R;
import com.bombon.voxr.fragment.AboutFragment;
import com.bombon.voxr.fragment.HistoryFragment;
import com.bombon.voxr.fragment.MainFragment;
import com.bombon.voxr.fragment.TestFragment;
import com.bombon.voxr.model.User;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.Util;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchAnimListener;
import com.mahfa.dnswitch.DayNightSwitchListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vaughn on 9/5/17.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private final String TAG = this.getClass().getSimpleName();

    @Inject
    UserService userService;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;
    private HeaderViewHolder headerViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Dagger init
        getComponent().inject(this);

        //Backstack manager
        this.getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupToolbar();
        setupDrawer();
        setupNavigationView();

        // Check Permissions
        setupPermissionWrapper();

        changeFragment(new TestFragment());
    }

    private void setupPermissionWrapper() {
        MultiplePermissionsListener dialogMultiplePermissionsListener =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(this)
                        .withTitle("Recorder and Storage Permission")
                        .withMessage("Both audio recorder and storage permission are needed")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WAKE_LOCK
                ).withListener(dialogMultiplePermissionsListener).check();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerToggle.isDrawerIndicatorEnabled()) {
                    onBackPressed();
                }
            }
        });

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setupNavigationView() {
        // Navigation Item Default State
        navigationView.getMenu().getItem(0).setChecked(true);

        // NavigationView Click Listener
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation View Holder
        View header = navigationView.getHeaderView(0);
        headerViewHolder = new HeaderViewHolder(header);

        // Fill data to UI
        User user = userService.getLoggedIn();
        headerViewHolder.tvName.setText(user.getName());
        headerViewHolder.tvUsername.setText(user.getUsername());

        // Navigation Dark Mode Toggle
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                headerViewHolder.toggleDarkMode.setIsNight(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                headerViewHolder.toggleDarkMode.setIsNight(true);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
        }

        // Dark Mode
        headerViewHolder.toggleDarkMode.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean b) {
                // Dark Mode
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    return;
                }
                // Day
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        headerViewHolder.toggleDarkMode.setAnimListener(new DayNightSwitchAnimListener() {
            @Override
            public void onAnimStart() {

            }

            @Override
            public void onAnimEnd() {
                recreate();
            }

            @Override
            public void onAnimValueChanged(float v) {

            }
        });


        // User Profile
        headerViewHolder.btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        Util.displayAlert(this, null, "Are you sure you want to sign out?", "Sign out", "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userService.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.isChecked()) {
            return false;
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();

        Log.e(TAG, item.getItemId() + "");
        switch (item.getItemId()) {
            case R.id.nav_home:
                Log.e(TAG, "HOME");
                break;
            case R.id.nav_history:
                Log.e(TAG, "HISTORY");
                changeFragment(new HistoryFragment(), false);
                break;
            case R.id.nav_help:
                Log.e(TAG, "HELP");
                navigationView.setCheckedItem(R.id.nav_home);
                showHelpOverlay();
                break;
            case R.id.nav_about:
                Log.e(TAG, "ABOUT");
                changeFragment(new AboutFragment(), false);
                break;
            default:
                return false;
        }
        return false;
    }

    private void showHelpOverlay() {
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
        if (f instanceof MainFragment) {
            ((MainFragment) f).showHelpOverlay();
        }

    }

    @Override
    public void onBackStackChanged() {
        Fragment current = getCurrentFragment();
        Log.e(TAG, current.getClass().getSimpleName());
        if (current instanceof TestFragment) {
            drawerToggle.setDrawerIndicatorEnabled(true);
            navigationView.setCheckedItem(R.id.nav_home);
        } else {
            drawerToggle.setDrawerIndicatorEnabled(false);
        }
    }

    protected static class HeaderViewHolder {
        @BindView(R.id.toggle_dark_mode)
        DayNightSwitch toggleDarkMode;

        @BindView(R.id.name)
        TextView tvName;

        @BindView(R.id.username)
        TextView tvUsername;

        @BindView(R.id.btn_signout)
        TextView btnSignout;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
    }

    // Fragment Transaction
    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
    }

    private void changeFragment(Fragment fragment, boolean addToBackStack) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).addToBackStack(null).commit();
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }
}
