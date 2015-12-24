package com.fing.flowscan.fragement;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fing.flowscan.R;
import com.fing.flowscan.lisenter.BinderCallback;
import com.fing.flowscan.model.ProcessInfo;
import com.fing.flowscan.model.TrafficInfo;
import com.fing.flowscan.service.ProcessQueryService;
import com.fing.flowscan.utils.FUtil;
import com.fing.flowscan.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by fing on 2015/12/22.
 * Time 下午 12:15
 */
public class ProcessFragment extends Fragment {
    ListView listView;
    PackageManager pm;
//    List<PackageInfo> packageInfos;
    Intent intent;
    Button btn_all, btn_today, btn_hour, btn_month;
    List<Button> buttons;
    ProcessAdapter pAdapter;
    ServiceConnection conn;
    ProcessQueryService.ProcessQueryBinder binder;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_process, container, false);
        buttons = new ArrayList<>();
        btn_all = FUtil.$(view, R.id.bt_all);
        buttons.add(btn_all);
        btn_hour = FUtil.$(view, R.id.bt_hour);
        buttons.add(btn_hour);
        btn_month = FUtil.$(view, R.id.bt_month);
        buttons.add(btn_month);
        btn_today = FUtil.$(view, R.id.bt_today);
        buttons.add(btn_today);
        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Button bt : buttons) {
                        if (bt.getId() == v.getId()) {
                            bt.setTextColor(getResources().getColor(R.color.md_green_800));
                        } else {
                            bt.setTextColor(getResources().getColor(R.color.md_blue_400));
                        }
                    }
                    Intent pIntent = new Intent("com.fing.flowscan.ProcessQueryReceiver");
                    switch (v.getId()) {
                        case R.id.bt_all:
                            binder.setProcessStyle("all");
                            break;
                        case R.id.bt_hour:
                            binder.setProcessStyle("all");
                            break;
                        case R.id.bt_month:
                            binder.setProcessStyle("all");
                            break;
                        case R.id.bt_today:
                            binder.setProcessStyle("all");
                            break;
                    }
                }
            });
        }

        listView = FUtil.$(view, R.id.list_process);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pm = getActivity().getPackageManager();
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.arg1==1){
                    pAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (ProcessQueryService.ProcessQueryBinder) service;
                binder.getService().setCallback(new BinderCallback() {
                    @Override
                    public void call() {
                        pAdapter.getInfos().clear();
                        pAdapter.getInfos().addAll(binder.getProcessInfos());
                        Message msg = new Message();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);

                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };


        List<ProcessInfo> pInfos = new ArrayList<>();

        pAdapter = new ProcessAdapter(pInfos);
        listView.setAdapter(pAdapter);

        intent = new Intent(getActivity(), ProcessQueryService.class);
        getActivity().bindService(intent,conn,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(conn);
        super.onDestroy();
    }

    public class ProcessAdapter extends BaseAdapter {
        ViewHolder holder = null;
        List<ProcessInfo> pInfos;

        public ProcessAdapter(List<ProcessInfo> pInfos) {
            this.pInfos = pInfos;
        }
        public List<ProcessInfo> getInfos(){
            return pInfos;
        }

        @Override
        public int getCount() {
            return pInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return pInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_items_process, parent, false);
                holder = new ViewHolder();
                holder.tvUsed = FUtil.$(convertView, R.id.process_used);
                holder.tvSend = FUtil.$(convertView, R.id.process_send);
                holder.tvReceive = FUtil.$(convertView, R.id.process_receive);
                holder.tvName = FUtil.$(convertView, R.id.process_name);
                holder.icon = FUtil.$(convertView, R.id.process_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                ApplicationInfo aInfo = pm.getApplicationInfo(pInfos.get(position).getPackageName(), PackageManager
                        .GET_ACTIVITIES);
                holder.tvName.setText(pm.getApplicationLabel(aInfo));
                holder.icon.setImageDrawable(pm.getApplicationIcon(aInfo));
                holder.tvReceive.setText(FUtil.formatByte(pInfos.get(position).getReceive()));
                holder.tvSend.setText(FUtil.formatByte(pInfos.get(position).getSend()));
                holder.tvUsed.setText(FUtil.formatByte(pInfos.get(position).getReceive() + pInfos.get(position).getSend()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return convertView;
        }

        private class ViewHolder {
            TextView tvUsed, tvSend, tvReceive;
            TextView tvName;
            ImageView icon;
        }


    }

}
