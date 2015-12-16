package com.fing.flowscan.fragement;

import android.app.Fragment;
import android.content.BroadcastReceiver;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fing.flowscan.R;
import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.lisenter.TimeChooseLisenter;
import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.service.DayTrafficService;
import com.fing.flowscan.utils.FUtil;
import com.fing.flowscan.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fing on 2015/12/13.
 * Time 下午 03:56
 */
public class DayTrafficFragment extends BaseDayFragment {
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traffic_list, container, false);
        initView(view);
        listView = FUtil.$(view, R.id.ls_time_traffic);
        final List<Map<String, String>> list = getList();
        final SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                list, R.layout.list_items_traffic, new String[]{"time",
                "mobileSend", "mobileReceive", "wifiSend", "wifiReceive"}, new int[]{R.id.tv_time, R.id
                .tv_2g_send, R.id.tv_2g_receive, R.id.tv_wifi_send, R.id.tv_wifi_receive});

        listView.setAdapter(adapter);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                list.clear();
                List<Map<String, Long>> mapList = (List<Map<String, Long>>) msg.obj;
                for(Map<String,Long> map : mapList){
                    Map<String,String> listMap = new HashMap<>();
                    listMap.put("time",map.get("time")+"时");
                    listMap.put("mobileSend",FUtil.formatByte(map.get("mobileSend")));
                    listMap.put("mobileReceive",FUtil.formatByte(map.get("mobileReceive")));
                    listMap.put("wifiSend",FUtil.formatByte(map.get("wifiSend")));
                    listMap.put("wifiReceive",FUtil.formatByte(map.get("wifiReceive")));
                    list.add(listMap);
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return view;
    }

    private List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i <= 22; i += 2) {
            Map<String, String> map = new HashMap<>();
            map.put("time", i + "时");
            map.put("mobileSend", "0.00B");
            map.put("mobileReceive", "0.00B");
            map.put("wifiSend", "0.00B");
            map.put("wifiReceive", "0.00B");
            list.add(map);
        }
        return list;
    }


}
