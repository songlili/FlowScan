package com.fing.flowscan.fragement;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fing.flowscan.R;
import com.fing.flowscan.utils.FUtil;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.achartengine.chart.PointStyle.*;

/**
 * Created by fing on 2015/12/15.
 * Time 下午 09:22
 */
public class DayGraphicalFragment extends BaseDayFragment {
    LinearLayout linearLayout;
    XYMultipleSeriesDataset dataset;
    GraphicalView graphicalView;
    XYSeries mobileSend = new XYSeries("2G/3G发送");
    XYSeries mobileReceive = new XYSeries("2G/3G接收");
    XYSeries wifSend = new XYSeries("wifi发送");
    XYSeries wifiReceive = new XYSeries("wifi接收");
    XYMultipleSeriesRenderer renderer;
    // 进行显示
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        linearLayout = FUtil.$(view, R.id.graphical_view);
        initView(view);
        renderer = getRenderer();
        dataset = new XYMultipleSeriesDataset();
        reSetDataset(initList());
        graphicalView = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
        linearLayout.addView(graphicalView);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                List<Map<String, Long>> mapList = (List<Map<String, Long>>) msg.obj;
                reSetDataset(mapList);
                renderer.setChartTitle(editTime.getText().toString()+"流量统计");
                graphicalView.repaint();
                return true;
            }
        });
        return view;
    }

    private XYMultipleSeriesRenderer getRenderer() {
        // 1, 构造显示用渲染图
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setXTitle("时间/h");
        renderer.setYTitle("流量/MB");
        // 设置背景
        renderer.setApplyBackgroundColor(true);
        renderer.setMarginsColor(getActivity().getResources().getColor(R.color.md_grey_600));
        renderer.setBackgroundColor(getActivity().getResources().getColor(R.color.md_grey_600));

        renderer.setAxisTitleTextSize(20);
        renderer.setLabelsTextSize(24);
        //设置x轴不可以滑动
        renderer.setPanEnabled(false, true);
        //设置x数轴
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(24);
        renderer.setXLabels(12);
        // 上，左，下，右
        renderer.setMargins(new int[]{60, 45, 45, 25});
        // 设置标题
        renderer.setChartTitle(editTime.getText().toString()+"流量统计");
        renderer.setChartTitleTextSize(48);
        renderer.setLegendTextSize(24);

        // 3, 对点的绘制进行设置
        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        xyRenderer.setLineWidth(3);
        // 3.1设置颜色
        xyRenderer.setColor(Color.BLUE);
        // 3.2设置点的样式
        xyRenderer.setPointStyle(SQUARE);
        // 3.3, 将要绘制的点添加到坐标绘制中
        renderer.addSeriesRenderer(xyRenderer);
        // 3.4,重复 1~3的步骤绘制第二个系列点
        xyRenderer = new XYSeriesRenderer();
        xyRenderer.setColor(Color.RED);
        xyRenderer.setPointStyle(CIRCLE);
        renderer.addSeriesRenderer(xyRenderer);
        // 3, 对点的绘制进行设置
        xyRenderer = new XYSeriesRenderer();
        // 3.1设置颜色
        xyRenderer.setColor(Color.GREEN);
        // 3.2设置点的样式
        xyRenderer.setPointStyle(SQUARE);
        // 3.3, 将要绘制的点添加到坐标绘制中
        renderer.addSeriesRenderer(xyRenderer);
        // 3.4,重复 1~3的步骤绘制第二个系列点
        xyRenderer = new XYSeriesRenderer();
        xyRenderer.setColor(Color.YELLOW);
        xyRenderer.setPointStyle(CIRCLE);
        renderer.addSeriesRenderer(xyRenderer);
        return renderer;
    }

    private void reSetDataset(List<Map<String, Long>> mapList) {

        dataset.clear();
        /**
         *  map.put("mobileSend", FUtil.formatByte(info1.getMobileSend()+info2.getMobileSend()));
         map.put("mobileReceive", FUtil.formatByte(info1.getMobileReceive()+info2.getMobileReceive()));
         map.put("wifiSend", FUtil.formatByte(info1.getWifiSend()+info2.getWifiSend()));
         map.put("wifiReceive", FUtil.formatByte(info1.getWifiReceive()+info2.getWifiReceive()));
         */

        mobileReceive.clear();
        mobileSend.clear();
        wifiReceive.clear();
        wifSend.clear();
        for (Map<String, Long> map : mapList) {
            long xTime = map.get("time");
            double mSend = map.get("mobileSend");
            double mReceive = map.get("mobileReceive");
            double wSend = map.get("wifiSend");
            double wReceive = map.get("wifiReceive");
            mobileSend.add(xTime, mSend / (1024*1024));
            mobileReceive.add(xTime, mReceive / (1024*1024));
           // mobileReceive.addAnnotation(FUtil.formatByte((long) mReceive),xTime, mReceive / (1024*1024));
            wifSend.add(xTime, wSend / (1024*1024));
           // wifSend.addAnnotation(FUtil.formatByte((long) wSend),xTime, wSend / (1024*1024));
            wifiReceive.add(xTime, wReceive / (1024*1024));
            //wifiReceive.addAnnotation(FUtil.formatByte((long) wReceive),xTime, wReceive / (1024*1024));
        }
        dataset.addSeries(mobileReceive);
        dataset.addSeries(mobileSend);
        dataset.addSeries(wifiReceive);
        dataset.addSeries(wifSend);
    }
    private List<Map<String,Long>> initList(){
        List<Map<String,Long>> list = new ArrayList<>();
        for(int i=0; i<=22;i++){
            Map<String,Long> map = new HashMap<>();
            map.put("time", (long) i);
            map.put("mobileSend", 0L);
            map.put("mobileReceive", 0L);
            map.put("wifiSend", 0L);
            map.put("wifiReceive", 0L);
            list.add(map);
        }
        return list;
    }
}
