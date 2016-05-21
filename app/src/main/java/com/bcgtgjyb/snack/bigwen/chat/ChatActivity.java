package com.bcgtgjyb.snack.bigwen.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bcgtgjyb.snack.R;
import com.bcgtgjyb.snack.bigwen.chat.bean.BaseMessage;
import com.bcgtgjyb.snack.bigwen.chat.bean.ServiceUser;
import com.bcgtgjyb.snack.bigwen.chat.bean.User;
import com.bcgtgjyb.snack.bigwen.chat.tcp.GameThread;
import com.bcgtgjyb.snack.bigwen.chat.tcp.PacketSender;
import com.bcgtgjyb.snack.bigwen.chat.tcp.PacketType;
import com.bcgtgjyb.snack.bigwen.chat.tcp.SendCallback;
import com.bcgtgjyb.snack.bigwen.chat.view.ChatAdapter;
import com.bcgtgjyb.snack.bigwen.chat.view.KeyBoardListener;
import com.bcgtgjyb.snack.bigwen.chat.view.MessageUtil;
import com.bcgtgjyb.snack.bigwen.chat.view.SenderView;
import com.bcgtgjyb.snack.bigwen.protobuf.Notice;
import com.bcgtgjyb.snack.bigwen.sqlite.BwSqlUtil;

import java.util.List;

/**
 * Created by bigwen on 2016/5/15.
 */
public class ChatActivity extends Activity {

    private BroadcastReceiver mBroadcastReceiver;
    private GameThread gameThread;
    private ListView chatList;
    //    private EditText editText;
//    private Button button;
    private ChatAdapter mChatAdapter;
    public static String RESENDACTION = "GameActivity_ReSend";
    private String TAG = ChatActivity.class.getSimpleName();
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
        initID();

        gameThread = new GameThread("192.168.1.233", 7850);
        gameThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initSender();
            }
        }).start();


    }

    private void initID() {
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ChatActivity.this,SettingActivity.class));
            }
        });
    }


    private void initListView() {
        mChatAdapter = new ChatAdapter(this);
        chatList.setAdapter(mChatAdapter);

        List objects = BwSqlUtil.getInstance().loadObject(new BaseMessage());
        List<BaseMessage> baseMessage = (List<BaseMessage>) objects;
        if (baseMessage != null) {
            mChatAdapter.setData(baseMessage);
        }
    }

    private void getEdit() {

    }

    private void sendChatText(final String text) {
        if ("".equals(text)) {
            return;
        }
        BaseMessage bm = MessageUtil.makeTextMessage(text, User.uid, ServiceUser.uid, 0, 0);
        PacketSender.sendMessage(gameThread, bm, new SendCallback() {
            @Override
            public void onSuccess() {
                senderView.clearEdit();
                BaseMessage bm = MessageUtil.makeTextMessage(text, User.uid, ServiceUser.uid, 0, 1);
                BwSqlUtil.getInstance().saveObject(bm);
                mChatAdapter.addMessage(bm);
            }

            @Override
            public void onFailed(Exception e) {
                senderView.clearEdit();
                BaseMessage baseMessage = MessageUtil.makeTextMessage(text, User.uid, ServiceUser.uid, 0, 0);
                BwSqlUtil.getInstance().saveObject(baseMessage);
                mChatAdapter.addMessage(baseMessage);
            }
        });
    }

    private void reSendText(final BaseMessage baseMessage) {
        PacketSender.sendMessage(gameThread, baseMessage.chat_text, User.uid, ServiceUser.uid, new SendCallback() {
            @Override
            public void onSuccess() {
                baseMessage.isSendSuccess = 1;
                BwSqlUtil.getInstance().saveObject(baseMessage);
                mChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    public void reSend(int p) {
        Log.i(TAG, "reSend: ");
        BaseMessage baseMessage = (BaseMessage) mChatAdapter.getItem(p);
        if (baseMessage.type == 0) {
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
                        int p = intent.getIntExtra("position", -1);
                        reSend(p);
                        break;
                    case "rs_util_heartbeat":
                        Bundle bundle = intent.getExtras();
                        Notice.rs_util_heartbeat rs = (Notice.rs_util_heartbeat) bundle.get("rs_util_heartbeat");
//                        ToastUtil.show("服务器的heart:" + rs.getCode());
                        break;
                    case "chat_message":
                        Bundle b = intent.getExtras();
                        Notice.chat_message rq = (Notice.chat_message) b.get("chat_message");
//                        ToastUtil.show("服务器的heart:"+rq.getRsText());
                        BaseMessage baseMessage = MessageUtil.makeTextMessage(rq.getRqText(), rq.getSendid(), rq.getReceiverid(), 0, 1, rq.getTime());
                        mChatAdapter.addMessage(baseMessage);
                        break;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PacketType.getInstance().getPocketName(1));
        intentFilter.addAction(PacketType.getInstance().getPocketName(2));
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

    @Override
    protected void onPause() {
        super.onPause();
    }


    private boolean isShowKeyBoard = false;
    private void initKeyBoard() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        keyBoardListener = new KeyBoardListener(this, new KeyBoardListener.KeyBoradCallback() {
            @Override
            public void hideKeyBoard() {
                Log.i(TAG, "hideKeyBoard: ");
                if (isShowKeyBoard){
                    isShowKeyBoard = false;
                    senderView.closeBoard();
                }
            }

            @Override
            public void showKeyBoard() {
                Log.i(TAG, "showKeyBoard: ");
                isShowKeyBoard = true;
                senderView.closeBoard();
            }
        });
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyBoardListener);
    }

//    private void moveToLastOne(){
//        chatList.setSelection(mChatAdapter.getCount()-1);
//    }

    @SuppressLint("NewApi")
    private void removeGlobal() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(keyBoardListener);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (senderView.isBoard()) {
                senderView.closeBoard();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
