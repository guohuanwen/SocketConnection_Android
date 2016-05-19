package com.bcgtgjyb.snack.bigwen.game.view;

/**
 * Created by bigwen on 2016/5/19.
 */
public interface SenderInterface {
    //显示emoji面板
    void showEmojiBoard();
    //显示消息面板
    void showVoiceBt();
    //显示功能面板
    void showFunBoard();
    //发送消息
    void sendContent();
    //录语音
    void recordVoice();
    //添加图片
    void addPhoto();
    //取消语音
    void quitVioce();
}
