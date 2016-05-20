package com.bcgtgjyb.snack.bigwen.chat.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private LinearLayout funBoard;
    private LinearLayout emojiBoard;
    private boolean isEdit = true;
    private boolean canSend = false;
    private String TAG = SenderView.class.getSimpleName();
    private SenderListener senderListener;
    private boolean isBoardShow = false;
    private Handler mHander;

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
        mHander = new Handler(Looper.getMainLooper());
        LayoutInflater.from(mContext).inflate(R.layout.sender_view,this);
        voiceTv = (TextView) findViewById(R.id.sender_view_send_voice);
        voiceTouch = (TextView) findViewById(R.id.sender_view_voice_touch);
        editView = (EditText) findViewById(R.id.sender_view_edit);
        emojiTv = (TextView) findViewById(R.id.sender_view_emoji);
        sendTv = (TextView) findViewById(R.id.sender_view_send);
        funBoard = (LinearLayout) findViewById(R.id.sender_view_fun_board);
        emojiBoard = (LinearLayout) findViewById(R.id.sender_view_emoji_board);
        showFunBt();
        initOnTouch();
    }

    private void initOnTouch() {
        emojiTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emojiBoard.getVisibility() == View.VISIBLE){
                    hideEmojiBoard();
                }else {
                    closeKeyBoard();
                    mHander.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showEmojiBoard();
                        }
                    },200);

                }
            }
        });

        editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: s:"+s+";statr:"+start+";count:"+count+";after:"+after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "beforeTextChanged: s:"+s+";statr:"+start+";count:"+count+";before:"+before);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: "+s);
                if (s.length()>0 && isEdit){
                    showSend();
                    canSend = true;
                }else {
                    showFunBt();
                    canSend = false;
                }
            }
        });

        sendTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit&&canSend){
                    //发送
                    String text = editView.getText().toString();
                    if (senderListener != null && !text.isEmpty()){
                        senderListener.sendText(text);
                    }
                }
                if (!canSend){
                    if (funBoard.getVisibility() == View.VISIBLE){
                        hideFunBoard();
                    }else {
                        closeKeyBoard();
                        mHander.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showFunBoard();
                            }
                        },200);
                    }
                }
            }
        });

        voiceTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voiceTouch.getVisibility() == View.VISIBLE){
                    hideVoiceBt();
                }else {
                    showVoiceBt();
                }
            }
        });
    }

    @Override
    public void showEmojiBoard() {
        emojiBoard.setVisibility(VISIBLE);
        funBoard.setVisibility(GONE);
        voiceTouch.setVisibility(GONE);
        editView.setVisibility(VISIBLE);
        isEdit = true;
        closeKeyBoard();
    }

    @Override
    public void hideEmojiBoard() {
        emojiBoard.setVisibility(GONE);
        funBoard.setVisibility(GONE);
    }

    @Override
    public void showVoiceBt() {
        editView.setVisibility(GONE);
        voiceTouch.setVisibility(VISIBLE);
        emojiBoard.setVisibility(GONE);
        funBoard.setVisibility(GONE);
        isEdit = false;
    }

    @Override
    public void hideVoiceBt() {
        editView.setVisibility(VISIBLE);
        voiceTouch.setVisibility(GONE);
        emojiBoard.setVisibility(GONE);
        funBoard.setVisibility(GONE);
        isEdit = true;
    }

    @Override
    public void hideFunBoard() {
        emojiBoard.setVisibility(GONE);
        funBoard.setVisibility(GONE);
    }

    @Override
    public void showFunBoard() {
        emojiBoard.setVisibility(GONE);
        funBoard.setVisibility(VISIBLE);
        closeKeyBoard();
    }

    @Override
    public void sendContent() {
        String text = editView.getText().toString();
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

    public boolean isBoard(){
        if (emojiBoard.getVisibility() == View.VISIBLE){
            return true;
        }
        if (funBoard.getVisibility() == View.VISIBLE){
            return true;
        }
        return false;
    }

    public void showSend(){
        sendTv.setText("发送");
    }

    public void showFunBt(){
        sendTv.setText("加");
    }

    @Override
    public void closeBoard() {
        hideFunBoard();
        hideEmojiBoard();
        isBoardShow = true;
    }

    public void clearEdit(){
        editView.setText("");
    }

    public void setSenderListener(SenderListener senderListener) {
        this.senderListener = senderListener;
    }

    public void closeKeyBoard(){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity)mContext).getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public interface SenderListener{
        void sendText(String text);
    }
}
