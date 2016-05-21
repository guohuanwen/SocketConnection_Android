package com.bcgtgjyb.snack.bigwen.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.bcgtgjyb.snack.bigwen.chat.GameServer;
import com.bcgtgjyb.snack.bigwen.chat.bean.User;
import com.bcgtgjyb.snack.bigwen.tool.SharePreUtil;

/**
 * Created by bigwen on 2016/5/1.
 */
public class MyApplication extends Application {

    private static Context mContext;
    private GameServer gameServer;
    private static String TAG = MyApplication.class.getSimpleName();
    private static MyApplication myApplicationp;
    private ServiceConnection serviceConnection;
    private boolean isBinder = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        myApplicationp = this;
        mContext = getApplicationContext();
        initID();
    }

    public static MyApplication getMyApplication() {
        return myApplicationp;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void doBindService(final BindCallback bindCallback){
        Log.i(TAG, "doBindService: ");
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected: ");
                gameServer = ((GameServer.BwBinder)service).getSeivice();
                bindCallback.onFinish();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected: ");
                gameServer = null;
            }
        };
        bindService(new Intent(this,GameServer.class),
                serviceConnection,Context.BIND_AUTO_CREATE);
        isBinder = true;
    }

    private void unBinderService(){
        if (isBinder){
            unbindService(serviceConnection);
            isBinder = false;
        }
    }

    private void initID() {
        User.uid = SharePreUtil.getInstance(this).getInt("uid");
    }

    public static Context getContext(){
        return mContext;
    }


    public interface BindCallback{
        void onFinish();
    }
}
