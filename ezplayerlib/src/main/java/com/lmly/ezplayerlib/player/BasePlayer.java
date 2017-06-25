package com.lmly.ezplayerlib.player;

import android.os.Handler;

import com.lmly.ezplayerlib.callback.OnCreatePlayerListen;
import com.lmly.ezplayerlib.callback.OnStartRealPlayerLsn;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZDeviceInfo;


/**
 * Created by lmly-pc on 2017/6/25.
 */

public abstract class BasePlayer extends EZPlayer {
    protected Handler handler;
    protected EZPlayer ezPlayer;
    protected EZDeviceInfo ezDeviceInfo;
    protected EZOpenSDK ezOpenSDK;

    /**
     * 获取萤石播放器自己的实例，这个的对外公开是为了让使用者在使用时，需要调用我没封装的方法时，能够自己自定义，
     * 更方便一点，不过其实我只是懒得复写所有方法而已啦，别和我说什么设计规范，有种你别用，你来锤我啊。
     * @return
     */
    protected EZPlayer getEzPlayer(){
        return ezPlayer;
    }
    /**
     * 是否开始对讲
     */
    protected boolean isTalk=false;
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
