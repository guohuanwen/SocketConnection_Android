package com.bcgtgjyb.snack.bigwen.game;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bcgtgjyb.snack.bigwen.game.tcp.GameThread;


/**
 * Created by bigwen on 2016/5/15.
 */
public class GameServer extends Service {



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GameThread gameThread = new GameThread("192.168.1.234",7850);
        gameThread.start();
    }
}
