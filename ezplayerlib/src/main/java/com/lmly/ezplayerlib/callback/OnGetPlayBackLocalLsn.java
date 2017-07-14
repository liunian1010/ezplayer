package com.lmly.ezplayerlib.callback;

import com.videogo.openapi.bean.EZDeviceRecordFile;

import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author liumeiliya 2017/6/27 10:01
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017/6/27
 * @modify by reason:{方法名}:{原因}
 */

public interface OnGetPlayBackLocalLsn {
    void onSuccess(List<EZDeviceRecordFile> tmpList);
    void onError(int code, String msg);
}
