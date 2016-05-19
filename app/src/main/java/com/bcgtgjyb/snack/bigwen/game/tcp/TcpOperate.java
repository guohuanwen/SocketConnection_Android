package com.bcgtgjyb.snack.bigwen.game.tcp;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * Created by bigwen on 2016/5/17.
 */
public interface TcpOperate {
    //连接
    void connection(Socket socket, String ip, int port) throws Exception;
    //重连
    void reConnection(Socket socket, String ip, int port) throws Exception;
    //断开
    void disConnection(Socket socket) throws Exception;
    //读取包
    void readPocket(DataInputStream dataInputStream) throws Exception;
    //发送包
    void sendPocket(byte[] bytes,SendCallback sendCallback);
    //清除缓冲等对象
    void clearSocket() throws Exception;
}
