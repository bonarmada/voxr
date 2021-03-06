package com.bombon.voxr.common.dagger.component;


import com.bombon.voxr.activity.ForgotPasswordActivity;
import com.bombon.voxr.activity.LoginActivity;
import com.bombon.voxr.activity.MainActivity;
import com.bombon.voxr.activity.RegisterActivity;
import com.bombon.voxr.common.dagger.module.DaoModule;
import com.bombon.voxr.common.dagger.module.PrefsModule;
import com.bombon.voxr.common.dagger.module.RemoteModule;
import com.bombon.voxr.common.dagger.scope.AppScope;
import com.bombon.voxr.fragment.AboutFragment;
import com.bombon.voxr.fragment.HistoryFragment;
import com.bombon.voxr.fragment.MainFragment;
import com.bombon.voxr.fragment.TestFragment;

import dagger.Component;

/**
 * Created by Vaughn on 6/8/17.
 */

@AppScope
@Component(modules = {RemoteModule.class, PrefsModule.class, DaoModule.class})

public interface AppComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(ForgotPasswordActivity activity);
    void inject(MainFragment fragment);
    void inject(TestFragment fragment);
    void inject(RegisterActivity fragment);
    void inject(HistoryFragment fragment);
    void inject(AboutFragment fragment);
}
