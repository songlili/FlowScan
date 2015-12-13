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
import com.fing.flowscan.service.DayTrafficService;
import com.fing.flowscan.utils.FUtil;
import com.fing.flowscan.utils.LogUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by fing on 2015/12/13.
 * Time 下午 03:56
 */
public class DayTrafficFragment extends Fragment {
    EditText editTime;
    ListView listView;
    Button btnSearch;
    DayTrafficService service;
    ServiceConnection conn;
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traffic_list, container, false);
        editTime = FUtil.$(view, R.id.edit_time);
        listView = FUtil.$(view, R.id.ls_time_traffic);

        btnSearch = FUtil.$(view, R.id.btn_search);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                List<Map<String, String>> list = (List<Map<String, String>>) msg.obj;
                SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                        list,R.layout.list_items_traffic, new String[]{"time",
                        "mobileSend", "mobileReceive", "wifiSend", "wifiReceive"}, new int[]{R.id.tv_time, R.id
                        .tv_2g_send, R.id.tv_2g_receive, R.id.tv_wifi_send, R.id.tv_wifi_receive});

                listView.setAdapter(adapter);
                return true;
            }
        });
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        editTime.setText(year + "年" + month + "月" + day + "日");
        editTime.setOnClickListener(new TimeChooseLisenter(getActivity()));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service != null)
                    service.setDay(getDay(editTime.getText().toString()));
            }
        });
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final DayTrafficService.DayTrafficBinder binder = (DayTrafficService.DayTrafficBinder) service;
                DayTrafficFragment.this.service = binder.getService();
                DayTrafficFragment.this.service.setDay(getDay(editTime.getText().toString()));
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
        return view;
    }

    private String getDay(String str) {
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
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("-----------", "unbind");
        getActivity().unbindService(conn);
    }

}
