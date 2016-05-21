package com.bcgtgjyb.snack.bigwen.chat.tcp;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by bigwen on 2016/5/17.
 */
public class PacketType {

    private String TAG = PacketType.class.getSimpleName();
    private String[] pocketTypes;
    private HashMap<String, Integer> pocketToType = new HashMap<>();
    private static PacketType pocketType;

    public static PacketType getInstance(){
        if (pocketType == null){
            synchronized (PacketType.class){
                if (pocketType == null){
                    pocketType = new PacketType();
                }
            }
        }
        return pocketType;
    }

    private PacketType() {
        pocketTypes = new String[]{"rq_util_heartbeat", "rs_util_heartbeat","chat_message"};
        for (int i = 0; i < pocketTypes.length; i++) {
            pocketToType.put(pocketTypes[i], i);
        }
    }

    public int getPocketType(String pocket) {
        return pocketToType.get(pocket);
    }

    public String getPocketName(int type) {
        Log.i(TAG, "getPocketName: i="+type+";total="+pocketTypes.length);
        if (type <0 || type >=pocketTypes.length){
            return "";
        }
        return pocketTypes[type];
    }
}
