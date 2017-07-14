package com.lmly.ezplayerlib.callback;


import com.videogo.openapi.EZConstants;
import com.videogo.openapi.bean.EZVideoQualityInfo;

import java.util.ArrayList;

/**
 * Created by lmly-pc on 2017/6/25.
 */

public interface OnCreatePlayerListen {
    void onSuccess();
    void onGetVideoQuality(ArrayList<EZVideoQualityInfo> ezVideoQualityInfos, EZConstants.EZVideoLevel mCurrentQulityMode);
    void onError(int code,int msg,String content);
}
