package com.bcgtgjyb.snack.bigwen.chat.view;

import com.bcgtgjyb.snack.bigwen.chat.bean.BaseMessage;

/**
 * Created by bigwen on 2016/5/19.
 */
public class MessageUtil {

    public static BaseMessage makeTextMessage(String text,int sendId,int receiverId,int type,int isSendSuccess){
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.chat_text = text;
        baseMessage.sendId = sendId;
        baseMessage.receiverId = receiverId;
        baseMessage.type = type;
        baseMessage.chat_time = System.currentTimeMillis();
        baseMessage.isSendSuccess = isSendSuccess;
        return baseMessage;
    }

    public static BaseMessage makeTextMessage(String text,int sendId,int receiverId,int type,int isSendSuccess,long time){
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.chat_text = text;
        baseMessage.sendId = sendId;
        baseMessage.receiverId = receiverId;
        baseMessage.type = type;
        baseMessage.chat_time = time;
        baseMessage.isSendSuccess = isSendSuccess;
        return baseMessage;
    }

}
