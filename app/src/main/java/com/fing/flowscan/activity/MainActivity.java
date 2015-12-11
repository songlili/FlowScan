package com.fing.flowscan.activity;


import android.os.Bundle;
import android.os.PersistableBundle;

import com.fing.flowscan.R;

/**
 * Created by fing on 2015/12/11.
 */
public class MainActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_time;
    }
}
