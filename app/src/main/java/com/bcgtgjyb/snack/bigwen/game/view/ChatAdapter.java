package com.bcgtgjyb.snack.bigwen.game.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bcgtgjyb.snack.bigwen.game.bean.BaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigwen on 2016/5/19.
 */
public class ChatAdapter extends BaseAdapter {


    private Context mContext;
    private List<BaseMessage> messages = new ArrayList<BaseMessage>();

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<BaseMessage> baseMessages){
        messages.clear();
        messages.addAll(baseMessages);
        notifyDataSetChanged();
    }

    public void addMessage(BaseMessage baseMessage){
        messages.add(baseMessage);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatItem chatItem = null;
        if (convertView == null){
            chatItem = new ChatItem(mContext);
        }else {
            chatItem = (ChatItem)convertView;
        }
        chatItem.setView(messages.get(position));
        return chatItem;
    }
}
