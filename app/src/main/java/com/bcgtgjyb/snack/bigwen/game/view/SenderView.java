package com.bcgtgjyb.snack.bigwen.game.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcgtgjyb.snack.R;

/**
 * Created by bigwen on 2016/5/19.
 */
public class SenderView extends LinearLayout implements SenderInterface{

    private Context mContext;
    private TextView voiceTv;
    private TextView voiceTouch;
    private EditText editView;
    private TextView emojiTv;
    private TextView sendTv;

    public SenderView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.sender_view,this);
        voiceTv = (TextView) findViewById(R.id.sender_view_send_voice);
        voiceTouch = (TextView) findViewById(R.id.sender_view_voice_touch);
        editView = (EditText) findViewById(R.id.sender_view_edit);
        emojiTv = (TextView) findViewById(R.id.sender_view_emoji);
        sendTv = (TextView) findViewById(R.id.sender_view_send);
    }

    @Override
    public void showEmojiBoard() {

    }

    @Override
    public void showVoiceBt() {

    }

    @Override
    public void showFunBoard() {

    }

    @Override
    public void sendContent() {

    }

    @Override
    public void recordVoice() {

    }

    @Override
    public void addPhoto() {

    }

    @Override
    public void quitVioce() {

    }
}
