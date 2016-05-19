package com.bcgtgjyb.snack.bigwen.game.view;

import com.bcgtgjyb.snack.bigwen.game.bean.BaseMessage;

/**
 * Created by bigwen on 2016/5/19.
 */
public class MessageUtil {

    public static BaseMessage makeTextMessage(String text,int sendId,int receiverId,int type){
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.text = text;
        baseMessage.sendId = sendId;
        baseMessage.receiverId = receiverId;
        baseMessage.type = type;
        return baseMessage;
    }
}
