package com.bombon.voxr.common.dagger.module;


import com.bombon.voxr.common.dagger.remote.PostRemote;
import com.bombon.voxr.common.dagger.scope.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Vaughn on 6/8/17.
 */

@Module(includes = NetworkModule.class)
public class RemoteModule {
    @Provides
    @AppScope
    PostRemote providePostRemote(Retrofit retrofit){
        return retrofit.create(PostRemote.class);
    }
}