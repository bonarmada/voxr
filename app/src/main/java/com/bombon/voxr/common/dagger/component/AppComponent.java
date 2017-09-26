package com.bombon.voxr.common.dagger.component;


import com.bombon.voxr.common.dagger.module.PrefsModule;
import com.bombon.voxr.common.dagger.module.RemoteModule;
import com.bombon.voxr.common.dagger.scope.AppScope;
import com.bombon.voxr.fragment.AboutFragment;
import com.bombon.voxr.fragment.HistoryFragment;
import com.bombon.voxr.fragment.MainFragment;

import dagger.Component;

/**
 * Created by Vaughn on 6/8/17.
 */

@AppScope
@Component(modules = {RemoteModule.class, PrefsModule.class})

public interface AppComponent {
    void inject(MainFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(AboutFragment fragment);
}
