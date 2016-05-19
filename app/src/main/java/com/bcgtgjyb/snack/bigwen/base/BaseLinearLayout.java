package com.bcgtgjyb.snack.bigwen.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by bigwen on 2016/5/19.
 */
public class BaseLinearLayout extends LinearLayout {

    public Context mContext;

    public BaseLinearLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void init() {

    }
}
