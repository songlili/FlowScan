package com.fing.flowscan.activity;


import android.os.Bundle;
import android.os.PersistableBundle;

import com.fing.flowscan.R;

/**
 * Created by fing on 2015/12/11.
 */
public class MainActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_traffic_list;
    }
}
