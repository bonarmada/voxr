package com.bombon.voxr.common.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.bombon.voxr.Constants;
import com.bombon.voxr.common.dagger.scope.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vaughn on 8/17/17.
 */

@Module(includes = {ContextModule.class})
public class PrefsModule {
    static final String PREFSKEY = Constants.Config.PREFSKEY;

    @Provides
    @AppScope
    SharedPreferences provideSharedPreference(Context context){
        return context.getSharedPreferences(PREFSKEY, Context.MODE_PRIVATE);
    }
}
