package com.bcgtgjyb.snack.bigwen.game.tcp;

import android.util.Log;

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
        send(rq_util_heartbeat,0,tcpOperate);
    }

    public static void sendMessage(TcpOperate tcpOperate,String text) throws Exception {
        Notice.rq_send_message rq_send_message = Notice.rq_send_message.newBuilder()
                .setRqText(text)
                .build();
        send(rq_send_message,2,tcpOperate);
    }

    private static void send(GeneratedMessage generatedMessage, int type, TcpOperate tcpOperate) throws Exception {
        byte[] bytes = generatedMessage.toByteArray();
        int length = bytes.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(8+length);
        byteBuffer.putInt(type);
        byteBuffer.putInt(length);
        byteBuffer.put(bytes);
        tcpOperate.sendPocket(byteBuffer.array());
    }
}
