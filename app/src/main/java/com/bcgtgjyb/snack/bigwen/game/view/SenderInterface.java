package com.bcgtgjyb.snack.bigwen.game.view;

/**
 * Created by bigwen on 2016/5/19.
 */
public interface SenderInterface {
    //显示emoji面板
    void showEmojiBoard();
    //隐藏emoji
    void hideEmojiBoard();
    //显示语音面板
    void showVoiceBt();
    //隐藏语音
    void hideVoiceBt();
    //显示功能面板
    void showFunBoard();
    //隐藏功能
    void hideFunBoard();
    //发送消息
    void sendContent();
    //录语音
    void recordVoice();
    //添加图片
    void addPhoto();
    //取消语音
    void quitVioce();
    //关闭键盘等
    void closeBoard();
}
