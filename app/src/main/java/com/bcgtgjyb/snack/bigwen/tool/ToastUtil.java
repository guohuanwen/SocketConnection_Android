package com.bcgtgjyb.snack.bigwen.tool;

import android.widget.Toast;

import com.bcgtgjyb.snack.bigwen.base.MyApplication;

/**
 * Created by bigwen on 2016/5/19.
 */
public class ToastUtil {

    public static void show(String text){
        Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_SHORT).show();
    }
}
