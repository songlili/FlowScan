package com.fing.flowscan.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by fing on 2015/12/11.
 */
public class BaseActivity extends AppCompatActivity {
    protected static String TAG;
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(int resId){
        return (T) findViewById(resId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(getContentView() != -1){
            setContentView(getContentView());
        }
        TAG = getClass().getSimpleName();
    }
    protected int getContentView(){
        return -1;
    }
}
