package com.fing.flowscan.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fing.flowscan.R;
import com.fing.flowscan.fragement.DayTrafficFragment;
import com.fing.flowscan.fragement.TrafficFragment;

/**
 * Created by fing on 2015/12/11.
 * Time 下午 09:13
 */
public class MainActivity extends BaseActivity {
    Fragment trafficFragment, dayTrafficFragment;

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

    public void btClick(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.btn_traffic:
                if (trafficFragment == null) {
                    trafficFragment = new TrafficFragment();
                }
                ft.replace(R.id.main_frame_layout, trafficFragment);
                break;
            case R.id.btn_day_traffic:
                if (dayTrafficFragment == null) {
                    dayTrafficFragment = new DayTrafficFragment();
                }
                ft.replace(R.id.main_frame_layout, dayTrafficFragment);
                break;
        }
        ft.commit();
    }
}
