package com.bcgtgjyb.snack.bigwen.chat.tcp;

import android.content.Intent;
import android.util.Log;

import com.bcgtgjyb.snack.bigwen.base.MyApplication;
import com.bcgtgjyb.snack.bigwen.protobuf.Notice;

import java.io.DataInputStream;

/**
 * Created by bigwen on 2016/5/17.
 */
public class PacketParse {
    private static String TAG = PacketParse.class.getSimpleName();

    public static void packet(DataInputStream dataInputStream) throws Exception{
        int type = PacketOrnament.readMessageType(dataInputStream);
        int length = PacketOrnament.readMessageLength(dataInputStream);
        byte[] bytes = new byte[length];
        dataInputStream.readFully(bytes);
        packetToClass(bytes,type);
    }

    private static void packetToClass(byte[] bytes,int type) throws Exception{
        Log.i(TAG, "packetToClass: "+type);
        switch (type){
            //心跳消息
            case 1:
                Notice.rs_util_heartbeat rs_util_heartbeat = Notice.rs_util_heartbeat.parseFrom(bytes);
                Intent intent = new Intent();
                intent.setAction(rs_util_heartbeat.getClass().getSimpleName());
                intent.putExtra(rs_util_heartbeat.getClass().getSimpleName(),rs_util_heartbeat);
                sendBroadcast(intent);
                break;
            //接收聊天文字消息
            case 3:
                Notice.rs_receiver_message rs_receiver_message = Notice.rs_receiver_message.parseFrom(bytes);
                Intent intent1 = new Intent();
                String action = rs_receiver_message.getClass().getSimpleName();
                intent1.setAction(action);
                intent1.putExtra(action,rs_receiver_message);
                sendBroadcast(intent1);
                break;
        }
    }

    private static void sendBroadcast(Intent intent){
        MyApplication.getContext().sendBroadcast(intent);
    }
}
