package com.bcgtgjyb.snack.tcp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bcgtgjyb.snack.MyApplication;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bigwen on 2016/4/28.
 */
public class SocketThread extends Thread {

    private Socket mSocket;
    private String HOST = "115.28.173.68";
    private int PORT = 10033;
    private boolean isConnect = false;
    private String TAG = SocketThread.class.getName();
    private ExecutorService writeExecutorService;
    private Handler mainHandler;
    public static int IS_CONNECTED = 2;
    public static int IS_CONNECTING = 1;
    public static int socketStatus = 0;
    private boolean isRun = false;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public SocketThread(String host, int port) {
        if (host != null) {
            this.HOST = host;
        }
        if (port != 0) {
            this.PORT = port;
        }
        writeExecutorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        isRun = true;
    }

    private void socketConnect() throws IOException {
        Log.i(TAG, "socketConnect: ");
        if (mSocket != null && mSocket.isConnected()) {
            return;
        }
        mSocket = new Socket(HOST, PORT);
        dataOutputStream = new DataOutputStream(mSocket.getOutputStream());
        dataInputStream = new DataInputStream(mSocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            socketConnect();
            socketStatus = IS_CONNECTED;
        } catch (Exception e) {
            Log.i(TAG, "run: " + "连接错误");
            e.printStackTrace();
            socketStatus = IS_CONNECTING;
            reConnect();
            socketStatus = IS_CONNECTED;
        }
        Log.i(TAG, "run: " + isRun);
        while (isRun) {
            try {
                final byte[] bytes = read();
                if (bytes == null) {
//                    Thread.sleep(3 * 1000);
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            packet(bytes);
                        }
                    });
                }
            } catch (Exception e) {
                try {
                    socketDisConnect();
                    socketConnect();
                    socketStatus = IS_CONNECTING;
                    reConnect();
                    socketStatus = IS_CONNECTED;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    private byte[] read() throws IOException {
        int type = dataInputStream.readInt();
        int length = dataInputStream.readInt();
        byte[] buffers = new byte[length];
        dataInputStream.readFully(buffers);
        return buffers;
    }

    private void reConnect() {
        Log.i(TAG, "reConnect: 1:" + isRun);
        while (true) {
            try {
                Thread.sleep(3 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "reConnect: 2");
            try {
                socketStatus = IS_CONNECTING;
                socketDisConnect();
                socketConnect();
                socketStatus = IS_CONNECTED;
                break;
            } catch (Exception e) {
                e.printStackTrace();
                socketStatus = IS_CONNECTING;
            }
        }
    }

    private void socketDisConnect() throws IOException {
        if (dataInputStream != null) dataInputStream.close();
        dataInputStream = null;
        if (dataOutputStream != null) dataOutputStream.close();
        dataOutputStream = null;
        if (mSocket != null) mSocket.close();
        mSocket = null;
    }

    public void write(final GeneratedMessage message, final int type, final WriteCallback writeCallback) {
        if (mSocket == null) {
            return;
        }
        if (socketStatus != IS_CONNECTED) {
            return;
        }
        if (dataOutputStream == null) {
            return;
        }
        writeExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] b = message.toByteArray();
                    Log.i(TAG, "run: " + b.length);
                    //类型
                    dataOutputStream.writeInt(type);
                    //长度
                    dataOutputStream.writeInt(b.length);
                    dataOutputStream.write(b);
                    dataOutputStream.flush();
                    writeCallback.onSuccess();
                } catch (Exception e) {
                    writeCallback.onFailed();
                    e.printStackTrace();
                } finally {

                }
            }
        });
    }

    public interface WriteCallback {
        void onSuccess();

        void onFailed();
    }

    private void packet(byte[] bytes) {
        try {
            Notice.rq_game_changeDirection rq_game_changeDirection = Notice.rq_game_changeDirection.parseFrom(bytes);
            String uid = rq_game_changeDirection.getUid();
            Log.i(TAG, "packet: " + uid);
            Toast.makeText(MyApplication.getContext(), "packet" + uid, Toast.LENGTH_SHORT).show();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public void setRun() {
        this.isRun = true;
    }

    public void closeThread() {
        isRun = false;
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
