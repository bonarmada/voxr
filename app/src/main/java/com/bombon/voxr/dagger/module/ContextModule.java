package com.bombon.voxr.dagger.module;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vaughn on 6/8/17.
 */

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }
}
