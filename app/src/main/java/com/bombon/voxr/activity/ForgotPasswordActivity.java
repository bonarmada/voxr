package com.bombon.voxr.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bombon.voxr.R;
import com.bombon.voxr.model.pojo.Password;
import com.bombon.voxr.service.UserService;
import com.bombon.voxr.util.ServiceCallback;
import com.bombon.voxr.util.Util;
import com.dd.CircularProgressButton;
import com.goodiebag.pinview.Pinview;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vaughn on 9/5/17.
 */

public class ForgotPasswordActivity extends BaseActivity {
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    // constants
    private static final int STATE_EMAIL_VERIFIED = 1;
    private static final int STATE_CODE_VERIFIED = 2;

    @Inject
    UserService userService;

    @BindView(R.id.btn_check)
    CircularProgressButton btnCheck;

    @BindView(R.id.btn_verify)
    CircularProgressButton btnVerify;

    @BindView(R.id.btn_reset)
    CircularProgressButton btnReset;

    @BindView(R.id.email)
    EditText etEmail;

    @BindView(R.id.pinview)
    Pinview pinView;

    @BindView(R.id.password)
    EditText etPassword;

    @BindView(R.id.confirm)
    EditText etConfirm;

    // Containers
    @BindView(R.id.verify_container)
    LinearLayout verifyContainer;
    @BindView(R.id.reset_container)
    LinearLayout resetContainer;

    @OnClick(R.id.btn_check)
    void checkOnClick() {
        checkEmail();
    }

    @OnClick(R.id.btn_verify)
    void verifyOnClick() {
        verifyCode();
    }

    @OnClick(R.id.btn_reset)
    void resetOnClick() {
        resetPassword();
    }

    private Password password = new Password();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);

        // Dagger init
        getComponent().inject(this);
    }


    private void checkEmail() {
        if (!isValidEmail())
            return;

        btnCheck.setIndeterminateProgressMode(true);
        btnCheck.setProgress(1);

        password.setEmail(etEmail.getText().toString());

        // Call API
        userService.forgot(password, new ServiceCallback() {
            @Override
            public void onSuccess(int statusCode, Object result) {
                btnCheck.setProgress(100);
                btnCheck.setEnabled(false);
                etEmail.setFocusable(false);
                etEmail.setEnabled(false);

                // Change ui state
                changeActivityState(STATE_EMAIL_VERIFIED);
            }

            @Override
            public void onError(int code, String message) {
                btnCheck.setProgress(0);
                etEmail.setError(message);
            }
        });
    }

    private void verifyCode() {
        if (!isValidCode())
            return;

        btnVerify.setIndeterminateProgressMode(true);
        btnVerify.setProgress(1);

        password.setCode(pinView.getValue());

        // Call API
        userService.verify(password, new ServiceCallback() {
            @Override
            public void onSuccess(int statusCode, Object result) {
                btnVerify.setProgress(100);
                btnVerify.setEnabled(false);
                pinView.setFocusable(false);
                pinView.setEnabled(false);

                //Change ui state
                changeActivityState(STATE_CODE_VERIFIED);
            }

            @Override
            public void onError(int statusCode, String message) {
                Log.e(TAG, message);
                btnVerify.setProgress(0);
                if (statusCode == 400)
                    Util.displayAlert(ForgotPasswordActivity.this, message, null);
            }
        });
    }

    private void resetPassword() {
        if (!isValidPassword())
            return;

        btnReset.setIndeterminateProgressMode(true);
        btnReset.setProgress(1);

        password.setPassword(etPassword.getText().toString());

        // Call API
        userService.reset(password, new ServiceCallback() {
            @Override
            public void onSuccess(int statusCode, Object result) {
                Util.displayAlert(ForgotPasswordActivity.this, "Successfully changed password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ForgotPasswordActivity.this.finish();
                    }
                });
            }

            @Override
            public void onError(int statusCode, String message) {
                Log.e(TAG, message);
                btnReset.setProgress(0);
                if (statusCode == 400)
                    Util.displayAlert(ForgotPasswordActivity.this, message, null);
            }
        });

    }

    private boolean isValidEmail() {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.error_field_required));
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidCode() {
        if (pinView.getValue().length() != 4) {
            return false;
        }
        return true;
    }


    private boolean isValidPassword() {
        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.error_field_required));
            etPassword.requestFocus();
            return false;
        }
        if (etConfirm.getText().toString().isEmpty()) {
            etConfirm.setError(getString(R.string.error_field_required));
            etConfirm.requestFocus();
            return false;
        }
        if (!etConfirm.getText().toString().equals(etPassword.getText().toString())) {
            etConfirm.setError(getString(R.string.error_password_not_match));
            etConfirm.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnCheck = null;
    }

    private void changeActivityState(int state) {
        switch (state) {
            case STATE_EMAIL_VERIFIED:
                verifyContainer.setVisibility(View.VISIBLE);
                break;
            case STATE_CODE_VERIFIED:
                resetContainer.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
