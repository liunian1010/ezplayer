package com.lmly.ezplayerlib.player;

import android.os.Handler;
import android.os.Message;

import com.lmly.ezplayerlib.callback.OnCreatePlayerListen;
import com.lmly.ezplayerlib.callback.OnStartRealPlayerLsn;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;

/**
 * 具体的播放器类
 * Created by lmly-pc on 2017/6/25.
 */
public class MyEZPlayer extends BasePlayer {
    OnCreatePlayerListen mOnCreatePlayerListen;
    OnStartRealPlayerLsn onStartRealPlayerLsn;



    public MyEZPlayer() {
        handler = new Handler();
    }

    private Handler EZhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    public void creatNewPlayer(final String deviceSerial,final int channelNo, OnCreatePlayerListen onCreatePlayerListen) {
        this.mOnCreatePlayerListen = onCreatePlayerListen;
        ezOpenSDK = EZOpenSDK.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ezDeviceInfo = ezOpenSDK.getDeviceInfo(deviceSerial);
                } catch (BaseException e) {
                    e.printStackTrace();
                    if (mOnCreatePlayerListen != null) {
                        mOnCreatePlayerListen.onError(e.getErrorCode(), -1, "");
                    }
                }
                if (ezDeviceInfo == null) {
                    if (mOnCreatePlayerListen != null) {
                        mOnCreatePlayerListen.onError(-1, -1, "未获取到设备信息");
                    }
                }
                ezPlayer.setHandler(EZhandler);
                ezPlayer = ezOpenSDK.createPlayer(deviceSerial, channelNo);
                if (mOnCreatePlayerListen != null) {
                    mOnCreatePlayerListen.onSuccess();
                }
            }
        }).start();
    }

    @Override
    public void startRealPlay(String deviceSerial, int channelNo, OnStartRealPlayerLsn onStartRealPlayerLsn) {
        this.onStartRealPlayerLsn = onStartRealPlayerLsn;
    }
}
