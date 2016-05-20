package com.bcgtgjyb.snack.bigwen.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.bcgtgjyb.snack.R;
import com.bcgtgjyb.snack.bigwen.game.bean.BaseMessage;
import com.bcgtgjyb.snack.bigwen.game.tcp.GameThread;
import com.bcgtgjyb.snack.bigwen.game.tcp.PacketSender;
import com.bcgtgjyb.snack.bigwen.game.tcp.PacketType;
import com.bcgtgjyb.snack.bigwen.game.tcp.SendCallback;
import com.bcgtgjyb.snack.bigwen.game.view.ChatAdapter;
import com.bcgtgjyb.snack.bigwen.game.view.KeyBoardListener;
import com.bcgtgjyb.snack.bigwen.game.view.MessageUtil;
import com.bcgtgjyb.snack.bigwen.game.view.SenderView;
import com.bcgtgjyb.snack.bigwen.protobuf.Notice;
import com.bcgtgjyb.snack.bigwen.tool.ToastUtil;

/**
 * Created by bigwen on 2016/5/15.
 */
public class GameActivity extends Activity {

    private BroadcastReceiver mBroadcastReceiver;
    private GameThread gameThread;
    private ListView chatList;
//    private EditText editText;
//    private Button button;
    private ChatAdapter mChatAdapter;
    public static String RESENDACTION = "GameActivity_ReSend";
    private String TAG = GameActivity.class.getSimpleName();
    private SenderView senderView;
    private KeyBoardListener keyBoardListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        chatList = (ListView) findViewById(R.id.activity_game_chat);
//        editText = (EditText) findViewById(R.id.activity_game_edit);
//        button = (Button) findViewById(R.id.activity_game_send);
        senderView = (SenderView) findViewById(R.id.activity_game_sender);

        senderView.setSenderListener(new SenderView.SenderListener() {
            @Override
            public void sendText(String text) {
                sendChatText(text);
            }
        });


        initBroadcast();
        initListView();
        initKeyBoard();

        gameThread = new GameThread("192.168.1.233", 7850);
        gameThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initSender();
            }
        }).start();

    }

    private void initListView() {
        mChatAdapter = new ChatAdapter(this);
        chatList.setAdapter(mChatAdapter);
    }

    private void getEdit(){

    }

    private void sendChatText(final String text) {
        if ("".equals(text)) {
            return;
        }
        PacketSender.sendMessage(gameThread, text, new SendCallback() {
            @Override
            public void onSuccess() {
                senderView.clearEdit();
                BaseMessage baseMessage = MessageUtil.makeTextMessage(text, 0, 1, 0,true);
                mChatAdapter.addMessage(baseMessage);
            }

            @Override
            public void onFailed(Exception e) {
                senderView.clearEdit();
                BaseMessage baseMessage = MessageUtil.makeTextMessage(text, 0, 1, 0,false);
                mChatAdapter.addMessage(baseMessage);
            }
        });
    }

    private void reSendText(final BaseMessage baseMessage) {
        PacketSender.sendMessage(gameThread, baseMessage.text, new SendCallback() {
            @Override
            public void onSuccess() {
                baseMessage.isSendSuccess = true;
                mChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    public void reSend(int p){
        Log.i(TAG, "reSend: ");
        BaseMessage baseMessage = (BaseMessage)mChatAdapter.getItem(p);
        if (baseMessage.type == 0){
            reSendText(baseMessage);
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
                switch (intent.getAction()) {
                    //重发
                    case "GameActivity_ReSend":
                        int p = intent.getIntExtra("position",-1);
                        reSend(p);
                        break;
                    case "rs_util_heartbeat":
                        Bundle bundle = intent.getExtras();
                        Notice.rs_util_heartbeat rs = (Notice.rs_util_heartbeat) bundle.get("rs_util_heartbeat");
                        ToastUtil.show("服务器的heart:" + rs.getCode());
                        break;
                    case "rs_receiver_message":
                        Bundle b = intent.getExtras();
                        Notice.rs_receiver_message rq = (Notice.rs_receiver_message) b.get("rs_receiver_message");
//                        ToastUtil.show("服务器的heart:"+rq.getRsText());
                        BaseMessage baseMessage = MessageUtil.makeTextMessage(rq.getRsText(), 1, 0, 0,true);
                        mChatAdapter.addMessage(baseMessage);
                        break;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PacketType.getInstance().getPocketName(1));
        intentFilter.addAction(PacketType.getInstance().getPocketName(3));
        intentFilter.addAction(RESENDACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        gameThread.stopConnect();

        removeGlobal();
    }


    private void initKeyBoard(){
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        keyBoardListener = new KeyBoardListener(this, new KeyBoardListener.KeyBoradCallback() {
            @Override
            public void hideKeyBoard() {
                Log.i(TAG, "hideKeyBoard: ");
            }

            @Override
            public void showKeyBoard() {
                Log.i(TAG, "showKeyBoard: ");
                senderView.closeBoard();
            }
        });
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyBoardListener);
    }

    @SuppressLint("NewApi")
    private void removeGlobal(){
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(keyBoardListener);
    }

}
