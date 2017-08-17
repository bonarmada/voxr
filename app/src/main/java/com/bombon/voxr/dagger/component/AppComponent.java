package com.bombon.voxr.dagger.component;


import com.bombon.voxr.activity.MainActivity;
import com.bombon.voxr.dagger.module.PrefsModule;
import com.bombon.voxr.dagger.module.RemoteModule;
import com.bombon.voxr.dagger.scope.AppScope;

import dagger.Component;

/**
 * Created by Vaughn on 6/8/17.
 */

@AppScope
@Component(modules = {RemoteModule.class, PrefsModule.class})

public interface AppComponent {
    void inject(MainActivity activity);
}
