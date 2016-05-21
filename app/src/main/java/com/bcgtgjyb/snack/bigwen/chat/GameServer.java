package com.bcgtgjyb.snack.bigwen.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.bcgtgjyb.snack.bigwen.chat.tcp.GameThread;


/**
 * Created by bigwen on 2016/5/15.
 */
public class GameServer extends Service {

    private GameThread gameThread;
    private BwBinder bwBinder = new BwBinder();
    private String TAG = GameServer.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return bwBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        gameThread = new GameThread("192.168.1.233", 7850);
        gameThread.start();
    }

    public class BwBinder extends Binder{
        public GameServer getSeivice(){
            return GameServer.this;
        }
    }

    public GameThread getGameThread() {
        return gameThread;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameThread.stopConnect();
    }


}
