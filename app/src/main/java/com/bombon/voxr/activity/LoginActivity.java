package com.bombon.voxr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bombon.voxr.R;
import com.bombon.voxr.model.User;
import com.bombon.voxr.service.ServiceCallback;
import com.bombon.voxr.service.UserService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vaughn on 8/14/17.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    UserService userService;

    @OnClick(R.id.btn_login)
    void loginOnClick(){
        login();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
    }
}
