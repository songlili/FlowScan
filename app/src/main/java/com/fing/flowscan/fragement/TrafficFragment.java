package com.fing.flowscan.fragement;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fing.flowscan.R;
import com.fing.flowscan.utils.ViewUtil;

/**
 * Created by fing on 2015/12/12.
 */
public class TrafficFragment extends Fragment {
    private TextView tv2GSendHour,tv2GSendToday,tv2GReceiveHour,tv2GReceiveToday,tv2GSendAll,tv2GReceiveAll;
    private TextView tvWifiSendHour,tvWifiSendToday,tvWifiReceiveHour,tvWifiReceiveToday,tvWifiSendAll,tvWifiReceiveAll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_traffic,container,false);
        initView(view);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        })
        return view;
    }
    public void initView(View view){
        tv2GSendHour = ViewUtil.$(view,R.id.tv_2g_send_hour);
        tv2GReceiveHour = ViewUtil.$(view,R.id.tv_2g_receive_hour);
        tv2GSendToday = ViewUtil.$(view,R.id.tv_2g_send_today);
        tv2GReceiveToday = ViewUtil.$(view,R.id.tv_2g_receive_today);
        tv2GSendAll = ViewUtil.$(view,R.id.tv_2g_send_all);
        tv2GReceiveAll = ViewUtil.$(view,R.id.tv_2g_receive_all);

        tvWifiSendHour = ViewUtil.$(view,R.id.tv_wifi_send_hour);
        tvWifiReceiveHour = ViewUtil.$(view,R.id.tv_wifi_receive_hour);
        tvWifiSendToday = ViewUtil.$(view,R.id.tv_wifi_send_today);
        tvWifiReceiveToday = ViewUtil.$(view,R.id.tv_wifi_receive_today);
        tvWifiSendAll = ViewUtil.$(view,R.id.tv_wifi_send_all);
        tvWifiReceiveAll = ViewUtil.$(view,R.id.tv_wifi_recive_all);
    }

}
