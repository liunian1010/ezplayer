package com.lmly.ezplayerlib.player;

import com.lmly.ezplayerlib.callback.OnCreatePlayerListen;
import com.lmly.ezplayerlib.callback.OnStartRealPlayerLsn;

/**
 * Created by lmly-pc on 2017/6/25.
 */

public abstract class BasePlayer {
    /**
     * 创建播放器
     * @param deviceSerial 设备序列号
     * @param channelNo 设备通道号
     * @param onCreatePlayerListen 回调
     */
    public abstract void creatNewPlayer(String deviceSerial,int channelNo, OnCreatePlayerListen onCreatePlayerListen);
    /**
     * 开始预览
     * @param deviceSerial 设备序列号
     * @param channelNo 设备通道号
     * @param onStartRealPlayerLsn 回调
     */
    public abstract void startRealPlay(String deviceSerial,int channelNo,OnStartRealPlayerLsn onStartRealPlayerLsn);
}
