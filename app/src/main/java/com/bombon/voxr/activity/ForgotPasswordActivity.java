package com.bombon.voxr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.bombon.voxr.R;
import com.bombon.voxr.service.UserService;

import javax.inject.Inject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vaughn on 9/5/17.
 */

public class ForgotPasswordActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    UserService userService;

    @BindView(R.id.btn_check)
    CircularProgressButton btnCheck;

    @BindView(R.id.email)
    EditText etEmail;


    @OnClick(R.id.btn_check)
    void checkOnClick(){
        checkEmail();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);

        // Dagger init
        getComponent().inject(this);
    }


    private void checkEmail(){
        userService.logout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        btnCheck.dispose();
    }
}
