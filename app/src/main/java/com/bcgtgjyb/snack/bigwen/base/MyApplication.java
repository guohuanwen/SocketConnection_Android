package com.bcgtgjyb.snack.bigwen.base;

import android.app.Application;
import android.content.Context;

import com.bcgtgjyb.snack.bigwen.chat.bean.User;
import com.bcgtgjyb.snack.bigwen.tool.SharePreUtil;

/**
 * Created by bigwen on 2016/5/1.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initID();
    }

    private void initID() {
        User.uid = SharePreUtil.getInstance(this).getInt("uid");
    }

    public static Context getContext(){
        return mContext;
    }
}
