package com.bcgtgjyb.snack.bigwen.chat.tcp;

import android.util.Log;

import com.bcgtgjyb.snack.bigwen.chat.bean.BaseMessage;
import com.bcgtgjyb.snack.bigwen.protobuf.Notice;
import com.google.protobuf.GeneratedMessage;

import java.nio.ByteBuffer;

/**
 * Created by bigwen on 2016/5/15.
 */
public class PacketSender {

    private static String TAG = PacketType.class.getSimpleName();

    public static void sendHeartbeat(TcpOperate tcpOperate) throws Exception{
        Log.i(TAG, "sendHeartbeat: ");
        Notice.rq_util_heartbeat rq_util_heartbeat = Notice.rq_util_heartbeat.newBuilder()
                .setKeepAlive(1)
                .build();
        send(rq_util_heartbeat, 0, tcpOperate, null);
    }

    public static void sendMessage(TcpOperate tcpOperate,BaseMessage baseMessage,SendCallback sendCallback)  {
        Notice.chat_message rq_send_message = Notice.chat_message.newBuilder()
                .setReceiverid(baseMessage.receiverId)
                .setSendid(baseMessage.sendId)
                .setRqText(baseMessage.chat_text)
                .setTime(System.currentTimeMillis())
                .build();
        send(rq_send_message,2,tcpOperate,sendCallback);
    }

    public static void sendMessage(TcpOperate tcpOperate,String text,int send,int receiver,SendCallback sendCallback)  {
        Notice.chat_message rq_send_message = Notice.chat_message.newBuilder()
                .setReceiverid(receiver)
                .setSendid(send)
                .setRqText(text)
                .setTime(System.currentTimeMillis())
                .build();
        send(rq_send_message,2,tcpOperate,sendCallback);
    }

    private static void send(GeneratedMessage generatedMessage, int type, TcpOperate tcpOperate,SendCallback sendCallback) {
        byte[] bytes = generatedMessage.toByteArray();
        int length = bytes.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(8+length);
        byteBuffer.putInt(type);
        byteBuffer.putInt(length);
        byteBuffer.put(bytes);
        tcpOperate.sendPocket(byteBuffer.array(),sendCallback);
    }
}
