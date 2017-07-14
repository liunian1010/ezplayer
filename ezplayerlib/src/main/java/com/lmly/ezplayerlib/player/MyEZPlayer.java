package com.lmly.ezplayerlib.player;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.lmly.ezplayerlib.callback.OnCaptureLsn;
import com.lmly.ezplayerlib.callback.OnCreatePlayerListen;
import com.lmly.ezplayerlib.callback.OnGetPlayBackLocalLsn;
import com.lmly.ezplayerlib.callback.OnRecordLsn;
import com.lmly.ezplayerlib.callback.OnStartRealPlayerLsn;
import com.lmly.ezplayerlib.util.AudioPlayUtil;
import com.lmly.ezplayerlib.util.EZUtils;
import com.videogo.exception.BaseException;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceRecordFile;
import com.videogo.util.SDCardUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * 具体的播放器类
 * Created by lmly-pc on 2017/6/25.
 */
public class MyEZPlayer extends BasePlayer {
    OnCreatePlayerListen mOnCreatePlayerListen;
    OnStartRealPlayerLsn onStartRealPlayerLsn;
    OnCaptureLsn onCaptureLsn;
    OnRecordLsn onRecordLsn;
    AudioPlayUtil mAudioPlayUtil = null;
    List<EZDeviceRecordFile> tmpList = null;
    SurfaceView surfaceView;

    public MyEZPlayer(Handler handler) {
        this.handler = handler;
    }

    public List<EZDeviceRecordFile> getRecordFileList() {
        return tmpList;
    }

    public EZDeviceRecordFile getRecordFile(int index) {
        if (tmpList == null || tmpList.size() <= index) {
            return null;
        } else {
            EZDeviceRecordFile ezDeviceRecordFile = new EZDeviceRecordFile();
            ezDeviceRecordFile.setStartTime(tmpList.get(index).getStartTime());
            ezDeviceRecordFile.setStopTime(tmpList.get(index).getStopTime());
            return ezDeviceRecordFile;
        }
    }

    @Override
    public void creatNewPlayer(final String deviceSerial, final int channelNo, OnCreatePlayerListen onCreatePlayerListen) {
        this.deviceSerial = deviceSerial;
        this.channelNo = channelNo;
        this.mOnCreatePlayerListen = onCreatePlayerListen;
        ezOpenSDK = EZOpenSDK.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ezDeviceInfo = ezOpenSDK.getDeviceInfo(deviceSerial);
                    //获取对讲信息
                    ezTalkbackCapability = ezDeviceInfo.isSupportTalk();
                    //获取视频清晰度信息
                    if (ezDeviceInfo != null) {
                        List<EZCameraInfo> l = ezDeviceInfo.getCameraInfoList();
                        if (l != null) {
                            for (EZCameraInfo ezCameraInfo : l) {
                                if (ezCameraInfo.getCameraNo() == channelNo) {
                                    ezVideoQualityInfos = ezCameraInfo.getVideoQualityInfos();
                                    mCurrentQulityMode = ezCameraInfo.getVideoLevel();
                                    if (ezVideoQualityInfos != null) {
                                        if (mOnCreatePlayerListen != null) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mOnCreatePlayerListen.onGetVideoQuality(ezVideoQualityInfos, mCurrentQulityMode);
                                                }
                                            });

                                        }
                                    }
                                }
                            }
                        } else {
                            if (mOnCreatePlayerListen != null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mOnCreatePlayerListen.onError(-1, -1, "未获取视频清晰度");
                                    }
                                });

                            }
                        }
                    }
                } catch (final BaseException e) {
                    e.printStackTrace();
                    if (mOnCreatePlayerListen != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mOnCreatePlayerListen.onError(e.getErrorCode(), -1, "");
                            }
                        });

                    }
                }
                if (ezDeviceInfo == null) {
                    if (mOnCreatePlayerListen != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mOnCreatePlayerListen.onError(-1, -1, "未获取到设备信息");
                            }
                        });

                    }
                }
                ezPlayer = ezOpenSDK.createPlayer(deviceSerial, channelNo);
                ezPlayer.setSurfaceHold(surfaceView.getHolder());
                ezPlayer.setHandler(handler);
                if (mOnCreatePlayerListen != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnCreatePlayerListen.onSuccess();
                        }
                    });

                }
            }
        }).start();
    }

    @Override
    public void getPlayeBack(final String mDeviceSerial, final int channelNo, final Calendar startTime, final Calendar endTime, final OnGetPlayBackLocalLsn onGetPlayBackLocalLsn) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tmpList = EZOpenSDK.getInstance().searchRecordFileFromDevice(mDeviceSerial, channelNo,
                            startTime, endTime);
                } catch (final BaseException e) {
                    e.printStackTrace();
                    if (onGetPlayBackLocalLsn != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onGetPlayBackLocalLsn.onError(e.getErrorCode(), "");
                            }
                        });
                    }
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onGetPlayBackLocalLsn.onSuccess(tmpList);
                    }
                });
            }
        }).start();
    }

    @Override
    public void startRealPlay(String deviceSerial, int channelNo, OnStartRealPlayerLsn onStartRealPlayerLsn) {
        this.onStartRealPlayerLsn = onStartRealPlayerLsn;
    }

    @Override
    public void startBackPlay(int index) {
        if (tmpList != null && tmpList.size() > 0 && ezPlayer != null) {
            Log.i("lmly", "index:" + index);
            ezPlayer.setHandler(handler);
            ezPlayer.setSurfaceHold(surfaceView.getHolder());
            ezPlayer.startPlayback(tmpList.get(index));
        }
    }

    @Override
    public void stopBackPlay() {
        if (ezPlayer != null) {
            ezPlayer.stopPlayback();
        }
    }

    @Override
    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    @Override
    public void capture(final String sourcePath, final String thumbnailPath, OnCaptureLsn onCaptureLsn2) {
        this.onCaptureLsn = onCaptureLsn2;
        if (!SDCardUtil.isSDCardUseable()) {
            if (this.onCaptureLsn != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCaptureLsn.onError("存储卡不可用");
                    }
                });
            }
            return;
        }
        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            if (this.onCaptureLsn != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCaptureLsn.onError("抓图失败,存储空间已满");
                    }
                });
            }
            return;
        }
        new Thread() {
            @Override
            public void run() {
                if (ezPlayer == null) {
                    return;
                }
                Bitmap bmp = ezPlayer.capturePicture();
                if (bmp != null) {
                    try {
                        if (mAudioPlayUtil != null) {
                            mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);
                        }
                        if (TextUtils.isEmpty(sourcePath) || TextUtils.isEmpty(thumbnailPath)) {
                            bmp.recycle();
                            bmp = null;
                            return;
                        }
                        EZUtils.saveCapturePictrue(sourcePath, thumbnailPath, bmp);
                        if (onCaptureLsn != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onCaptureLsn.onSuccess("抓图成功,请在图像管理中查看");
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (bmp != null) {
                            bmp.recycle();
                        }
                    }
                }
                super.run();
            }
        }.start();
    }

    public boolean bIsRecording = false;

    @Override
    public void startRecord(final String recordFilePath, final String thumnailPath, OnRecordLsn onRecordLsn2) {
        this.onRecordLsn = onRecordLsn2;
        if (!SDCardUtil.isSDCardUseable()) {
            if (this.onCaptureLsn != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRecordLsn.onError("存储卡不可用");
                    }
                });
            }
            return;
        }
        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            if (this.onCaptureLsn != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRecordLsn.onError("抓图失败,存储空间已满");
                    }
                });
            }
            return;
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);

        if (ezPlayer != null) {
            if (ezPlayer == null) {
                return;
            }
            //这一段必须放在线程外面,不然录像文件很可能是空名字的,结果就会导致录像文件被覆盖的问题
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Bitmap bmp = ezPlayer.capturePicture();
                    if (bmp != null) {
                        try {
                            if (TextUtils.isEmpty(recordFilePath) || TextUtils.isEmpty(thumnailPath)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            File file = new File(thumnailPath);
                            if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            EZUtils.saveCapturePictrue(null, thumnailPath, bmp);
                        } catch (InnerException e) {
                            e.printStackTrace();
                        } finally {
                            if (bmp != null) {
                                bmp.recycle();
                            }
                        }
                    }
                }
            }.start();
            if (ezPlayer.startLocalRecordWithFile(recordFilePath)) {
                bIsRecording = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRecordLsn.onStart();
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRecordLsn.onError("录像失败");
                    }
                });
            }
        }
    }

    @Override
    public void stopRecord() {
        if (!bIsRecording) {
            return;
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        if (ezPlayer != null) {
            ezPlayer.stopLocalRecord();
        }
        bIsRecording = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                onRecordLsn.onFinsh("录像保存完成");
            }
        });
    }

    @Override
    public void startVoiceTalk() {

    }

    @Override
    public void stopVoiceTalk() {

    }

    @Override
    public boolean openSound() {
        if (ezPlayer != null) {
            return ezPlayer.openSound();
        }
        return false;
    }

    @Override
    public boolean closeSound() {
        if (ezPlayer != null) {
            return ezPlayer.closeSound();
        }
        return false;
    }

    @Override
    public void initAudio(Application application) {
        mAudioPlayUtil = AudioPlayUtil.getInstance(application);
    }

    public void release() {
        if (ezPlayer != null) {
            ezPlayer.release();
        }
        this.mOnCreatePlayerListen = null;
        this.onStartRealPlayerLsn = null;
        this.onCaptureLsn = null;
        this.onRecordLsn = null;
    }
}
