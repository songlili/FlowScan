package com.fing.flowscan.lisenter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fing.flowscan.R;

import java.util.Calendar;

/**
 * Created by fing on 2015/12/13.
 * Time 下午 06:49
 */
public class TimeChooseLisenter implements View.OnClickListener {
    Context context;

    public TimeChooseLisenter(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        final EditText edit = (EditText) v;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                edit.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        }, year, month, day);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_time, null);
//        builder.setTitle("请选择日期")
//                .setView(view)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        builder.show();
    }
}
