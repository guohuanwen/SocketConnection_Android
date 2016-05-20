package com.bcgtgjyb.snack.bigwen.game.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by bigwen on 2016/5/20.
 */
public class KeyBoardListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private String TAG = KeyBoardListener.class.getSimpleName();
    private Context context;
    private KeyBoradCallback keyBoardCallback;

    public KeyBoardListener(Context context,KeyBoradCallback keyBoardListener) {
        this.context = context;
        this.keyBoardCallback = keyBoardListener;
    }

    @Override
    public void onGlobalLayout() {
        if (keyBoardCallback != null){
            if (isKeyShown(context)){
                keyBoardCallback.showKeyBoard();
            }else {
                keyBoardCallback.hideKeyBoard();
            }
        }
    }

    private boolean isKeyboardShown(Context context) {
        final View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        Log.i(TAG, "isKeyboardShown: "+heightDiff+"  "+rootView.getRootView().getHeight()+"  "+rootView.getBottom());
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private boolean isKeyShown(Context context){
        final View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        Log.i(TAG, "isKeyboardShown: "+"  "+rootView.getRootView().getHeight()+"  "+rootView.getBottom());
        return rootView.getRootView().getHeight() != rootView.getBottom();
    }


    public interface KeyBoradCallback{
        void hideKeyBoard();
        void showKeyBoard();
    }
}
