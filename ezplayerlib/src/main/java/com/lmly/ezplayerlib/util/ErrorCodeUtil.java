package com.lmly.ezplayerlib.util;

import com.videogo.exception.ErrorCode;
import com.videogo.exception.InnerException;

/**
 * <p>类说明</p>
 *
 * @author liumeiliya 2017/7/12 19:27
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017/7/12
 * @modify by reason:{方法名}:{原因}
 */

public  class ErrorCodeUtil {
    public static String getErrorDetail(int code){
        String txt;
        switch (code) {
            case ErrorCode.ERROR_CAS_CONNECT_FAILED:
                txt = "连接服务器失败，请重试";
                break;
            case 2004:
                txt = "播放失败，连接设备异常";
                break;
            case 2003:
                txt = "设备不在线";
                break;
            case ErrorCode.ERROR_CAS_PLATFORM_CLIENT_REQUEST_NO_PU_FOUNDED:
                txt = "设备不在线";
                break;
            case InnerException.INNER_DEVICE_NOT_EXIST:
                txt = "设备不在线";
                break;
            case ErrorCode.ERROR_STREAM_VTDU_STATUS_500:
                txt = "流媒体服务器内部处理错误";
                break;
            case ErrorCode.ERROR_STREAM_VTDU_STATUS_410:
                txt = "设备达到最大连接数";
                break;
            case ErrorCode.ERROR_STREAM_VTDU_STATUS_402:
                txt = "回放找不到录像文件";
                break;
            default:
                txt = "回放发生未知错误 errorCode:" + code;
                break;
        }
        return txt;
    }
}
