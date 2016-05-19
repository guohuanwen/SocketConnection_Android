package com.bcgtgjyb.snack.bigwen.game.tcp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bigwen on 2016/5/15.
 */
public class GameThread extends Thread implements TcpOperate {

    private String TAG = GameThread.class.getSimpleName();
    private Socket mSocket;
    private DataOutputStream dataOS;
    private DataInputStream dataIS;
    private String ip;
    private int port;
    private boolean isConnection = false;
    private boolean isRun = false;
    private ExecutorService executorService;
    private Handler mHandler;

    public GameThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
        isRun = true;
        executorService = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            connection(mSocket, ip, port);
            isConnection = true;
        } catch (Exception e) {
            try {
                reConnection(mSocket, ip, port);
                isConnection = true;
            } catch (Exception e1) {
                e1.printStackTrace();
                isConnection = false;
            }
        }
        while (isRun) {
            try {
                readPocket(dataIS);
            } catch (Exception e) {
                reConnection(mSocket, ip, port);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connection(Socket socket, String ip, int port) throws Exception {
        socket = new Socket(ip, port);
        dataIS = new DataInputStream(socket.getInputStream());
        dataOS = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void reConnection(Socket socket, String ip, int port) {
        disConnection(socket);
        while (isRun) {
            try {
                connection(socket, ip, port);
                isConnection = true;
                break;
            } catch (Exception e) {
                isConnection = false;
                e.printStackTrace();
            }
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disConnection(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataIS != null) {
            try {
                dataIS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataOS != null) {
            try {
                dataOS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readPocket(final DataInputStream dataInputStream) throws Exception {
        Log.i(TAG, "readPocket: ");
        PacketParse.packet(dataInputStream);

    }

    @Override
    public void sendPocket(final byte[] bytes, final SendCallback sendCallback) {
        Log.i(TAG, "sendPocket: " + isConnection);
        if (isConnection) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataOS.write(bytes);
                        dataOS.flush();
                        if (sendCallback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sendCallback.onSuccess();

                                }
                            });
                        }
                    } catch (final IOException e) {
                        isConnection = false;
                        reConnection(mSocket, ip, port);
                        e.printStackTrace();
                        if (sendCallback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sendCallback.onFailed(e);
                                }
                            });
                        }
                    }
                }
            });
        }else {
            if (sendCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendCallback.onFailed(new Exception("网络未连接"));
                    }
                });
            }
        }
    }

    @Override
    public void clearSocket() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataIS != null) {
            try {
                dataIS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataOS != null) {
            try {
                dataOS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopConnect() {
        isConnection = false;
        clearSocket();
    }
}
