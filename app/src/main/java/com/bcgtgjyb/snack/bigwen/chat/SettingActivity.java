package com.bcgtgjyb.snack.bigwen.chat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.bcgtgjyb.snack.R;
import com.bcgtgjyb.snack.bigwen.tool.SharePreUtil;

/**
 * Created by bigwen on 2016/5/21.
 */
public class SettingActivity extends Activity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_setting);
        editText = (EditText) findViewById(R.id.activity_setting_et);
        editText.setText(SharePreUtil.getInstance(this).getInt("uid")+"");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String text =  editText.getText().toString();
        int a = 1;
        try{
            a = Integer.valueOf(text);
        }catch (Exception e){
            e.printStackTrace();
        }
        SharePreUtil.getInstance(this).putInt("uid",a);
    }
}
