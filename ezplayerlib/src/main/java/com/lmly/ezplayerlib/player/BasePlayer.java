package com.lmly.ezplayerlib.player;

import android.app.Application;
import android.os.Handler;
import android.view.SurfaceView;

import com.lmly.ezplayerlib.callback.OnCaptureLsn;
import com.lmly.ezplayerlib.callback.OnCreatePlayerListen;
import com.lmly.ezplayerlib.callback.OnGetPlayBackLocalLsn;
import com.lmly.ezplayerlib.callback.OnRecordLsn;
import com.lmly.ezplayerlib.callback.OnStartRealPlayerLsn;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.openapi.bean.EZVideoQualityInfo;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by lmly-pc on 2017/6/25.
 */

public abstract class BasePlayer {
    /**
     * 设备对讲信息
     */
    protected EZConstants.EZTalkbackCapability ezTalkbackCapability;
    /**
     * 保存当前的视频码流清晰度
     */
    protected EZConstants.EZVideoLevel mCurrentQulityMode = EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED;
    /**
     * 用来存放监控点清晰度的列表
     */
    protected ArrayList<EZVideoQualityInfo> ezVideoQualityInfos;
    protected Handler handler;
    protected EZPlayer ezPlayer;
    protected EZDeviceInfo ezDeviceInfo;
    protected EZOpenSDK ezOpenSDK;
    protected String deviceSerial;
    protected  int channelNo;

    /**
     * 获取萤石播放器自己的实例，这个的对外公开是为了让使用者在使用时，需要调用我没封装的方法时，能够自己自定义，
     * 更方便一点，不过其实我只是懒得复写所有方法而已啦，别和我说什么设计规范，有种你别用，你来锤我啊。
     *
     * @return
     */
    public EZPlayer getEzPlayer() {
        return ezPlayer;
    }

    public  boolean isTalking = false;
    public  boolean isBackPlaying = false;
    public  boolean isRealPlaying = false;

    public abstract void creatNewPlayer(String deviceSerial, int channelNo, OnCreatePlayerListen onCreatePlayerListen);

    public abstract void getPlayeBack(String mDeviceSerial, int channelNo, Calendar startTime, Calendar endTime, OnGetPlayBackLocalLsn onGetPlayBackLocalLsn);

    public abstract void startRealPlay(String deviceSerial, int channelNo, OnStartRealPlayerLsn onStartRealPlayerLsn);

    public abstract void startBackPlay(int index);

    public abstract void stopBackPlay();

    public abstract void setSurfaceView(SurfaceView surfaceView);

    public abstract void capture(String sourcePath, String thumbnailPath, OnCaptureLsn onCaptureLsn);

    public abstract void startRecord(String recordFilePath,String thumnailPath,OnRecordLsn onRecordLsn);

    public abstract void stopRecord();

    public abstract void startVoiceTalk();

    public abstract void stopVoiceTalk();

    public abstract boolean openSound();

    public abstract boolean closeSound();

    public abstract void initAudio(Application application);
}
