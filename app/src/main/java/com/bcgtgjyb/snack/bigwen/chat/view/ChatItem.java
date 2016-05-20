package com.bcgtgjyb.snack.bigwen.chat.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcgtgjyb.snack.R;
import com.bcgtgjyb.snack.bigwen.chat.ChatActivity;
import com.bcgtgjyb.snack.bigwen.chat.bean.BaseMessage;
import com.bcgtgjyb.snack.bigwen.chat.bean.User;

/**
 * Created by bigwen on 2016/5/19.
 */
public class ChatItem extends LinearLayout {

    private Context mContext;
    private RelativeLayout leftLay;
    private RelativeLayout rightLay;
    private ImageView leftImg;
    private ImageView rightImg;
    private TextView leftName;
    private TextView rightName;
    private RelativeLayout leftContent;
    private RelativeLayout rightContent;
    private TextView resend;
    private int position;
    private String TAG = ChatItem.class.getSimpleName();

    //type=0
    private TextView leftType0;
    private TextView rightType0;


    private int type;

    public ChatItem(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ChatItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.chat_item, this);
        leftLay = (RelativeLayout) findViewById(R.id.chat_item_left_view);
        rightLay = (RelativeLayout) findViewById(R.id.chat_item_right_view);
        leftImg = (ImageView) findViewById(R.id.chat_item_left_head);
        rightImg = (ImageView) findViewById(R.id.chat_item_right_head);
        leftName = (TextView) findViewById(R.id.chat_item_left_name);
        rightName = (TextView) findViewById(R.id.chat_item_right_name);
        leftContent = (RelativeLayout) findViewById(R.id.chat_item_left_content);
        rightContent = (RelativeLayout) findViewById(R.id.chat_item_right_content);
        resend = (TextView) findViewById(R.id.chat_item_resend);

        rightType0 = (TextView) findViewById(R.id.chat_item_right_type_0);
        leftType0 = (TextView) findViewById(R.id.chat_item_left_type_0);
    }

    public void setView(BaseMessage baseMessage, final int position) {
        this.position = position;
        if (baseMessage.type == 0) {
            setType0(baseMessage);
            if (baseMessage.isSendSuccess == 1){
                resend.setVisibility(View.GONE);
            }else {
                resend.setVisibility(View.VISIBLE);
                resend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: ");
                        Intent intent = new Intent();
                        intent.putExtra("position",position);
                        intent.setAction(ChatActivity.RESENDACTION);
                        mContext.sendBroadcast(intent);
                    }
                });
            }
        }
    }

    //type = 0
    public void setType0(BaseMessage baseMessage) {
        boolean isRight = baseMessage.sendId == new User().uid;
        if (isRight) {
            setChildViewGone(false);
            rightType0.setVisibility(View.VISIBLE);
            rightType0.setText(baseMessage.chat_text);
            setView("",baseMessage.sendId+"",false,true);
        } else {
            setChildViewGone(true);
            leftType0.setVisibility(View.VISIBLE);
            leftType0.setText(baseMessage.chat_text);
            setView("",baseMessage.sendId+"",true,true);
        }
    }

    public void setView(String url, String name, boolean isLeft, boolean isShowName) {
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.
//                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams layoutParams0 = new RelativeLayout.LayoutParams(0, 0);
        if (isLeft) {
            rightLay.setVisibility(View.GONE);
            leftLay.setVisibility(View.VISIBLE);
            //加载图片

            leftName.setText(name);
//            if (isShowName) {
//                leftName.setLayoutParams(layoutParams);
//                leftName.setText(name);
//            } else {
//                leftName.setLayoutParams(layoutParams0);
//            }
        } else {
            rightLay.setVisibility(View.VISIBLE);
            leftLay.setVisibility(View.GONE);
            //加载图片

            rightName.setText(name);
//            if (isShowName) {
//                rightName.setLayoutParams(layoutParams);
//                rightName.setText(name);
//            } else {
//                rightName.setLayoutParams(layoutParams0);
//            }
        }
    }

    private void setChildViewGone(boolean isleft) {
        if (isleft) {
            leftContent.setVisibility(View.VISIBLE);
            rightContent.setVisibility(View.GONE);
            int countL = leftContent.getChildCount();
            for (int i = 0; i < countL; i++) {
                leftContent.getChildAt(i).setVisibility(View.GONE);
            }
        } else {
            leftContent.setVisibility(View.GONE);
            rightContent.setVisibility(View.VISIBLE);
            int countR = rightContent.getChildCount();
            for (int i = 0; i < countR; i++) {
                rightContent.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }
}
