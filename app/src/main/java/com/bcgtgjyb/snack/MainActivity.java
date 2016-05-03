package com.bcgtgjyb.snack;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bcgtgjyb.snack.tcp.Notice;
import com.bcgtgjyb.snack.tcp.SocketThread;

public class MainActivity extends AppCompatActivity {

    private EditText ipEt;
    private EditText portEt;
    private Button startBt;
    private SocketThread mSocketThread;
    private Context mContext;
    private TextView thisIpTv;
    private SharePreUtil mSharePreUtil;
    private String IP = "IP";
    private String PORT = "PORT";
    private String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipEt = (EditText) findViewById(R.id.ip);
        portEt = (EditText) findViewById(R.id.port);
        startBt = (Button) findViewById(R.id.start_button);
        thisIpTv = (TextView) findViewById(R.id.this_ip);
        mSharePreUtil = SharePreUtil.getInstance(this);

        String ip = mSharePreUtil.getString(IP);
        int port = mSharePreUtil.getInt(PORT);

        if (!"".equals(ip)) {
            ipEt.setText(ip);
        }
        if (port != -1) {
            portEt.setText(port + "");
        }
        thisIpTv.setText("ip:   " + getLocalMacAddressFromWifiInfo());

        mContext = this;
        initClient();
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
            }
        });
    }

    private String ip = "";
    private int port = 0;

    private void initClient() {

        if (isEditNull(ipEt)) {
            ip = ipEt.getText().toString();
        } else {
            return;
        }
        port = caculateNumber(portEt);
        if (port < 0) {
            return;
        }
        if (mSocketThread == null) {
            mSocketThread = new SocketThread(ip, port);
        }
        if (!mSocketThread.isAlive()) {
            mSocketThread.setRun();
            mSocketThread.start();
        }
    }

    private void write() {
        Log.i(TAG, "write: ");
        Notice.rq_game_changeDirection n = Notice.rq_game_changeDirection.newBuilder().setUid("guohuanwen").setDirection(1).build();
        if (mSocketThread != null) {
            mSocketThread.write(n,1, new SocketThread.WriteCallback() {
                @Override
                public void onSuccess() {
                    Log.i("MainActivity", "onSuccess: ");
                }

                @Override
                public void onFailed() {
                    Log.i("MainActivity", "onFailed: ");
                }
            });
        }

        if (isEditNull(ipEt)) {
            ip = ipEt.getText().toString();
        } else {
            return;
        }
        port = caculateNumber(portEt);
        if (port < 0) {
            return;
        }
        mSharePreUtil.putString(IP, ip);
        mSharePreUtil.putInt(PORT, port);
    }

    private boolean isEditNull(EditText editText) {
        if (editText == null) {
            return false;
        }
        if (editText.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private int caculateNumber(EditText editText) {
        if (!isEditNull(editText)) return -1;
        String numb = editText.getText().toString();
        try {
            return Integer.valueOf(numb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getLocalMacAddressFromWifiInfo() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAdress = info.getMacAddress(); //获取mac地址
        int ipAddress = info.getIpAddress();  //获取ip地址
        String ip = intToIp(ipAddress);
        return ip;
    }

    public String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocketThread != null) {
            mSocketThread.closeThread();
        }
    }
}
