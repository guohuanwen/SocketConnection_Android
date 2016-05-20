package com.bcgtgjyb.snack.bigwen.chat;

import android.util.Log;

import com.bcgtgjyb.snack.bigwen.base.MyApplication;
import com.bcgtgjyb.snack.bigwen.chat.bean.BaseMessage;
import com.github.lazylibrary.util.FileUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigwen on 2016/5/20.
 */
public class MessageManager {
    private static String MESSAGE = "chat_message";
    private static String TAG = MessageManager.class.getSimpleName();

    public static void saveChatMessage(List<BaseMessage> messages){
        Gson gson = new Gson();
        String text = gson.toJson(messages);
        Log.i(TAG, "saveChatMessage: "+text);
        boolean save = FileUtils.saveStrToFile(text, MyApplication.getContext().getFilesDir()+"/"+MESSAGE);
        Log.i(TAG, "saveChatMessage: save"+save);
    }

    public static List<BaseMessage> loadChatMessage(){
        Gson gson = new Gson();
        String text = FileUtils.readFile(MyApplication.getContext().getFilesDir()+"/"+MESSAGE,"UTF-8").toString();
        Log.i(TAG, "loadChatMessage: "+text);
        try {
            List<BaseMessage> messages = gson.fromJson(text, new ArrayList<BaseMessage>().getClass());
            return messages;
        }catch (Exception e){
            return null;
        }
    }

}
