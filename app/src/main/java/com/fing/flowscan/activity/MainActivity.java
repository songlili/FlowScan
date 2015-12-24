package com.fing.flowscan.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fing.flowscan.R;
import com.fing.flowscan.fragement.DayGraphicalFragment;
import com.fing.flowscan.fragement.DayTrafficFragment;
import com.fing.flowscan.fragement.ProcessFragment;
import com.fing.flowscan.fragement.TrafficFragment;
import com.fing.flowscan.service.TrafficInsertService;
import com.fing.flowscan.utils.LogUtil;

/**
 * Created by fing on 2015/12/11.
 * Time 下午 09:13
 */
public class MainActivity extends BaseActivity {
    Fragment trafficFragment, dayTrafficFragment, graphicalFragment,processFragment;
    Intent insertService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertService = new Intent(this, TrafficInsertService.class);
        startService(insertService);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(insertService);
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
            case R.id.btn_traffic_graphical:
                if (graphicalFragment == null) {
                    graphicalFragment = new DayGraphicalFragment();
                }
                ft.replace(R.id.main_frame_layout, graphicalFragment);
                break;
            case R.id.btn_process:
                if (processFragment == null) {
                    processFragment = new ProcessFragment();
                }
                ft.replace(R.id.main_frame_layout, processFragment);
                break;
        }
        ft.commit();
    }
}
