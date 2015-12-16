package com.fing.flowscan.fragement;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fing.flowscan.R;
import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.lisenter.TimeChooseLisenter;
import com.fing.flowscan.service.DayTrafficService;
import com.fing.flowscan.utils.FUtil;

import java.util.Calendar;

/**
 * Created by fing on 2015/12/16.
 * Time 下午 03:18
 */
public abstract class BaseDayFragment extends Fragment {
    protected DayTrafficService dayService;
    protected EditText editTime;
    protected Button btnSearch;
    protected ServiceConnection conn;
    protected Handler handler;

    protected void initView(View view) {
        editTime = FUtil.$(view, R.id.edit_time);
        btnSearch = FUtil.$(view, R.id.btn_search);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        editTime.setText(year + "年" + month + "月" + day + "日");
        editTime.setOnClickListener(new TimeChooseLisenter(getActivity()));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayService != null)
                    dayService.setDay(getDay(editTime.getText().toString()));
            }
        });
    }

    protected String getDay(String str) {
        String[] temp = str.split("年");
        String year = temp[0];
        temp = temp[1].split("月");
        String month = temp[0];
        String day = temp[1].split("日")[0];
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        month = m < 10 ? "0" + m : "" + m;
        day = d < 10 ? "0" + d : "" + d;
        String dayStr = year + "-" + month + "-" + day;
        return dayStr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final DayTrafficService.DayTrafficBinder binder = (DayTrafficService.DayTrafficBinder) service;
                dayService = binder.getService();
                dayService.setDay(getDay(editTime.getText().toString()));
                binder.getService().setCallback(new BinderCallback() {
                    @Override
                    public void call() {
                        Message msg = new Message();
                        msg.obj = binder.getList();
                        handler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent i = new Intent(getActivity(), DayTrafficService.class);
        getActivity().bindService(i, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(conn);
    }
}
