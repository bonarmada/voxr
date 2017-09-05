package com.bombon.voxr.common.dagger.component;


import com.bombon.voxr.activity.HomeActivity;
import com.bombon.voxr.common.dagger.module.PrefsModule;
import com.bombon.voxr.common.dagger.module.RemoteModule;
import com.bombon.voxr.common.dagger.scope.AppScope;

import dagger.Component;

/**
 * Created by Vaughn on 6/8/17.
 */

@AppScope
@Component(modules = {RemoteModule.class, PrefsModule.class})

public interface AppComponent {
    void inject(HomeActivity activity);
}
