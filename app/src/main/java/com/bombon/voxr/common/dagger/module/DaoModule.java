package com.bombon.voxr.common.dagger.module;


import com.bombon.voxr.common.dagger.scope.AppScope;
import com.bombon.voxr.dao.RecordDao;
import com.bombon.voxr.dao.UserDao;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vaughn on 8/18/17.
 */

@Module
public class DaoModule {
    @Provides
    @AppScope
    UserDao provideUserDao(){
        return new UserDao();
    }

    @Provides
    @AppScope
    RecordDao provideRecordDao(){
        return  new RecordDao();
    }
}
