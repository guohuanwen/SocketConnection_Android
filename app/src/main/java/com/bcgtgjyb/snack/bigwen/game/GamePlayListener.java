package com.bcgtgjyb.snack.bigwen.game;

/**
 * Created by bigwen on 2016/5/16.
 * 抽象出游戏中的所有互动点击事件
 */
public interface GamePlayListener {
    //看牌
    void lookCard();
    //跟注
    void betCoin();
    //加注
    void refuleCoin();
    //弃牌
    void givaUp();
    //比牌
    void compateCard();
    //打开语音听筒
    void openVoice();
    //打开麦克风
    void openMicroPhone();
}
