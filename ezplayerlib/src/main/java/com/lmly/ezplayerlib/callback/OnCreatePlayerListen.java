package com.lmly.ezplayerlib.callback;

/**
 * Created by lmly-pc on 2017/6/25.
 */

public interface OnCreatePlayerListen {
    void onSuccess();
    void onError(int code,int msg);
}
