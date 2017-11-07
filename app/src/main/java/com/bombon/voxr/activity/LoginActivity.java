package com.bombon.voxr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bombon.voxr.R;
import com.bombon.voxr.model.User;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.ErrorCode;
import com.bombon.voxr.util.ServiceCallback;
import com.bombon.voxr.util.Util;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Vaughn on 9/5/17.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    UserService userService;

    @OnClick(R.id.btn_login)
    void loginOnClick() {
        login();
    }

    @OnClick(R.id.btn_register)
    void registerOnClick() {
        register();
    }

    @OnClick(R.id.btn_forgot)
    void fogotOnClick() {
        forgotPassword();
    }

    @BindView(R.id.background)
    ImageView ivBackground;

    @BindView(R.id.btn_login)
    CircularProgressButton btnLogin;

    @BindView(R.id.btn_register)
    Button btnRegister;

    @BindView(R.id.username)
    EditText etUsername;

    @BindView(R.id.password)
    EditText etPassword;

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

        Picasso.with(this).load(R.drawable.blr2).fit().into(ivBackground);
    }



    private void login() {
        if (!isValid())
            return;

        // Valid text fields
        btnLogin.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                btnLogin.setText("Login");
            }
        });
        btnLogin.startAnimation();
        userService.login(new User(etUsername.getText().toString(), etPassword.getText().toString()), new ServiceCallback<User>() {
            @Override
            public void onSuccess(int statusCode, User result) {
                btnLogin.revertAnimation();
                if (statusCode == 200)
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onError(int code, String message) {
                btnLogin.revertAnimation();
                Timber.tag(TAG).d(message);
                showNetworkError(message);
            }
        });
    }

    private void register() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void forgotPassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }


    private void showNetworkError(String e) {
        Util.displayAlert(this, e, null);
    }

    private boolean isValid() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        btnLogin.dispose();
    }
}
