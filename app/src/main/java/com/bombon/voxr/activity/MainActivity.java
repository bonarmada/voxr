package com.bombon.voxr.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bombon.voxr.App;
import com.bombon.voxr.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App)getApplication()).getComponent().inject(this);
    }
}