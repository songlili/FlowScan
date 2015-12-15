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
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fing.flowscan.R;
import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.service.TrafficQueryService;
import com.fing.flowscan.utils.FUtil;

import java.util.Map;


/**
 * Created by fing on 2015/12/12.
 */
public class TrafficFragment extends Fragment {
    private TextView tv2GSendHour,tv2GSendToday,tv2GReceiveHour,tv2GReceiveToday,tv2GSendNow,tv2GReceiveNow;
    private TextView tvWifiSendHour,tvWifiSendToday,tvWifiReceiveHour,tvWifiReceiveToday,tvWifiSendNow,tvWifiReceiveNow;
    private ServiceConnection conn;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_traffic,container,false);
        initView(view);
        final Handler handler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg) {
                Map<String,TrafficInfo> map= (Map<String, TrafficInfo>) msg.obj;
                TrafficInfo nowInfo = map.get("now");
                if(nowInfo!=null) {
                    tv2GSendNow.setText(FUtil.formatByte(nowInfo.getmSx())+"/S");
                    tv2GReceiveNow.setText(FUtil.formatByte(nowInfo.getmRx())+"/S");
                    tvWifiSendNow.setText(FUtil.formatByte(nowInfo.getwSx())+"/S");
                    tvWifiReceiveNow.setText(FUtil.formatByte(nowInfo.getwRx())+"/S");
                }
                TrafficInfo hourInfo = map.get("hour");
                if(hourInfo != null) {
                    tv2GSendHour.setText(FUtil.formatByte(hourInfo.getMobileSend()));
                    tv2GReceiveHour.setText(FUtil.formatByte(hourInfo.getMobileReceive()));
                    tvWifiSendHour.setText(FUtil.formatByte(hourInfo.getWifiSend()));
                    tvWifiReceiveHour.setText(FUtil.formatByte(hourInfo.getWifiReceive()));
                }
                TrafficInfo dayInfo = map.get("day");
                if(dayInfo!=null) {
                    tv2GSendToday.setText(FUtil.formatByte(dayInfo.getMobileSend()));
                    tv2GReceiveToday.setText(FUtil.formatByte(dayInfo.getMobileReceive()));
                    tvWifiSendToday.setText(FUtil.formatByte(dayInfo.getWifiSend()));
                    tvWifiReceiveToday.setText(FUtil.formatByte(dayInfo.getWifiReceive()));
                }
                return true;
            }
        });
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final TrafficQueryService.TrafficBinder binder = (TrafficQueryService.TrafficBinder) service;

                binder.getService().setCallback(new BinderCallback(){
                    @Override
                    public void call() {
                        Map<String,TrafficInfo> map = new ArrayMap<>();
                       TrafficInfo nowInfo = binder.getNowInfo();
                       map.put("now",nowInfo);
                        TrafficInfo hourInfo = binder.getHourInfo();
                        map.put("hour",hourInfo);
                        TrafficInfo dayInfo = binder.getDayInfo();
                        map.put("day",dayInfo);
                        Message msg = new Message();
                        msg.obj = map;
                        handler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(getActivity(),TrafficQueryService.class);
       getActivity().bindService(intent,conn, Context.BIND_AUTO_CREATE);
        return view;
    }
    public void initView(View view){
        tv2GSendHour = FUtil.$(view,R.id.tv_2g_send_hour);
        tv2GReceiveHour = FUtil.$(view,R.id.tv_2g_receive_hour);
        tv2GSendToday = FUtil.$(view,R.id.tv_2g_send_today);
        tv2GReceiveToday = FUtil.$(view,R.id.tv_2g_receive_today);
        tv2GSendNow = FUtil.$(view,R.id.tv_2g_send_now);
        tv2GReceiveNow = FUtil.$(view,R.id.tv_2g_receive_now);

        tvWifiSendHour = FUtil.$(view,R.id.tv_wifi_send_hour);
        tvWifiReceiveHour = FUtil.$(view,R.id.tv_wifi_receive_hour);
        tvWifiSendToday = FUtil.$(view,R.id.tv_wifi_send_today);
        tvWifiReceiveToday = FUtil.$(view,R.id.tv_wifi_receive_today);
        tvWifiSendNow = FUtil.$(view,R.id.tv_wifi_send_now);
        tvWifiReceiveNow = FUtil.$(view,R.id.tv_wifi_recive_now);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(conn);
    }
}
