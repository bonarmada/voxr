package com.bombon.voxr.activity;

import android.support.v7.app.AppCompatActivity;

import com.bombon.voxr.App;
import com.bombon.voxr.dagger.component.AppComponent;

/**
 * Created by Vaughn on 8/14/17.
 */

public class BaseActivity extends AppCompatActivity {
    public AppComponent getComponent(){
        return ((App)getApplication()).getComponent();
    }
}
