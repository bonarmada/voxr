package com.bombon.voxr;


import com.bombon.voxr.common.dagger.component.AppComponent;
import com.bombon.voxr.common.dagger.component.DaggerAppComponent;
import com.bombon.voxr.common.dagger.module.ContextModule;
import com.bombon.voxr.common.dagger.module.NetworkModule;

/**
 * Created by Vaughn on 6/7/17.
 */

public class App extends android.app.Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .networkModule(new NetworkModule(Constants.BASE_URL))
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }
}