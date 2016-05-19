package com.bcgtgjyb.snack.bigwen.game.tcp;

import java.io.DataInputStream;

/**
 * Created by bigwen on 2016/5/17.
 */
public class PacketOrnament {

    public static void addMessageType(){

    }

    public static void addMessageLength(){

    }

    public static int readMessageType(DataInputStream dataInputStream) throws Exception{
        return dataInputStream.readInt();
    }

    public static int readMessageLength(DataInputStream dataInputStream) throws Exception{
        return dataInputStream.readInt();
    }
}
