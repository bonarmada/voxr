package com.bombon.voxr.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import com.bombon.voxr.R;
import com.bombon.voxr.model.User;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.ErrorCode;
import com.bombon.voxr.util.ErrorUtils;
import com.bombon.voxr.util.ServiceCallback;
import com.bombon.voxr.util.Util;

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

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Inject
    UserService userService;

    @Inject
    ErrorUtils errorUtils;

    @OnClick(R.id.btn_register)
    void registerOnClick() {
        register();
    }

    @BindView(R.id.username)
    EditText etUsername;

    @BindView(R.id.password)
    EditText etPassword;

    @BindView(R.id.first_name)
    EditText etFirstName;

    @BindView(R.id.last_name)
    EditText etLastName;

    @BindView(R.id.email)
    EditText etEmail;

    @BindView(R.id.btn_register)
    CircularProgressButton btnRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        // Dagger init
        getComponent().inject(this);
    }

    private void register() {
        if (!isValid())
            return;

        // Valid text fields
        btnRegister.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                btnRegister.setText("Create Account");
            }
        });
        btnRegister.startAnimation();

        userService.register(new User(etEmail.getText().toString(), etFirstName.getText().toString(), etLastName.getText().toString(), etUsername.getText().toString(), etPassword.getText().toString()), new ServiceCallback() {
            @Override
            public void onSuccess(int statusCode, Object result) {
                btnRegister.revertAnimation();
                if (statusCode == 201)
                    Util.displayAlert(RegisterActivity.this, "Registration Succesfull", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            }

            @Override
            public void onError(ErrorCode code, String message) {
                btnRegister.revertAnimation();
                Timber.tag(TAG).d(message);
                showNetworkError(message);
            }
        });
    }

    private void showNetworkError(String e) {
        Snackbar.make(findViewById(R.id.container),
                "e",
                Snackbar.LENGTH_LONG);
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
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.error_field_required));
            etEmail.requestFocus();
            return false;
        }
        if (etFirstName.getText().toString().isEmpty()) {
            etFirstName.setError(getString(R.string.error_field_required));
            etFirstName.requestFocus();
            return false;
        }
        if (etLastName.getText().toString().isEmpty()) {
            etLastName.setError(getString(R.string.error_field_required));
            etLastName.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        btnRegister.dispose();
    }
}
