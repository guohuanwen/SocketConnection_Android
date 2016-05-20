package com.bcgtgjyb.snack.bigwen.chat.bean;

/**
 * Created by bigwen on 2016/5/19.
 */
public class BaseMessage {
    //发送者的id
    public int sendId;
    //发给谁
    public int receiverId;
    //消息类型
    public int type;
    //消息时间
    public long chat_time;



    //type = 0
    public String chat_text;
    //type = 0

    //type = !
    //type = 1

    //type = 2
    //type = 2

    //type = 3
    //type = 3


    //本地使用属性
    public int isSendSuccess;
}
