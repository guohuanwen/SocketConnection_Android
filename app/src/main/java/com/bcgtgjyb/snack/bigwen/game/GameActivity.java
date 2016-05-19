package com.bcgtgjyb.snack.bigwen.game;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcgtgjyb.snack.R;
import com.bcgtgjyb.snack.bigwen.game.tcp.GameThread;
import com.bcgtgjyb.snack.bigwen.game.tcp.PacketSender;
import com.bcgtgjyb.snack.bigwen.game.tcp.PacketType;
import com.bcgtgjyb.snack.bigwen.tool.ToastUtil;
import com.bcgtgjyb.snack.bigwen.protobuf.Notice;

/**
 * Created by bigwen on 2016/5/15.
 */
public class GameActivity extends Activity {

    private BroadcastReceiver mBroadcastReceiver;
    private GameThread gameThread;
    private TextView chatText;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        chatText = (TextView) findViewById(R.id.activity_game_chat);
        editText = (EditText) findViewById(R.id.activity_game_edit);
        button = (Button) findViewById(R.id.activity_game_send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText();
            }
        });

        initBroadcast();
        gameThread = new GameThread("192.168.1.233", 7850);
        gameThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initSender();
            }
        }).start();
    }

    private void sendText(){
        String text = editText.getText().toString();
        if ("".equals(text)){
            return;
        }
        try {
            PacketSender.sendMessage(gameThread,text);
            editText.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSender() {
        while (true) {
            try {
                PacketSender.sendHeartbeat(gameThread);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case "rs_util_heartbeat":
                        Bundle bundle = intent.getExtras();
                        Notice.rs_util_heartbeat rs  = (Notice.rs_util_heartbeat)bundle.get("rs_util_heartbeat");
                        ToastUtil.show("服务器的heart:"+rs.getCode());
                        break;
                    case "rs_receiver_message":
                        Bundle b = intent.getExtras();
                        Notice.rs_receiver_message rq  = (Notice.rs_receiver_message)b.get("rs_receiver_message");
                        ToastUtil.show("服务器的heart:"+rq.getRsText());
                        break;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PacketType.getInstance().getPocketName(1));
        intentFilter.addAction(PacketType.getInstance().getPocketName(3));
        registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        gameThread.stopConnect();
    }

}
