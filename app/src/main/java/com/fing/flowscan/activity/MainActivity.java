package com.fing.flowscan.activity;


import android.content.Intent;
import android.os.Bundle;

import com.fing.flowscan.R;
import com.fing.flowscan.service.TrafficInsertService;

/**
 * Created by fing on 2015/12/11.
 * Time 下午 09:13
 */
public class MainActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        startService(new Intent("com.fing.service.TrafficInsertService"));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent("com.fing.service.TrafficInsertService"));
    }
}
