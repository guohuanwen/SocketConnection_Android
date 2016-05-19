package com.bcgtgjyb.snack.bigwen.tool;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bigwen on 2016/4/29.
 */
public class SharePreUtil {

    private static volatile SharePreUtil mSharePreUtil;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public static SharePreUtil getInstance(Context context) {
        if (mSharePreUtil == null) {
            synchronized (SharePreUtil.class) {
                if (mSharePreUtil == null) {
                    mSharePreUtil = new SharePreUtil(context);
                }
            }
        }
        return mSharePreUtil;
    }

    private SharePreUtil(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_APPEND);
    }

    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, -1);
    }
}
