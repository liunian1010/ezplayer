package com.lmly.ezplayerlib.util;

/**
 * <p>
 * 回放列表相关的工具函数
 * </p>
 *
 * @author hanlifeng 2014-6-16 下午1:53:03
 * @version V2.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-6-16
 * @modify by reason:{方法名}:{原因}
 */
public class RemoteListUtil {
    /**
     * <p>
     * 将录像时长转化为列表显示模式，比如"01:01:30"
     * </p>
     *
     * @param diffSeconds
     * @return
     * @author hanlifeng 2014-6-16 下午4:01:04
     */
    public static String convToUIDuration(int diffSeconds) {
        int min = diffSeconds / 60;
        String minStr = "";
        int sec = diffSeconds % 60;
        String secStr = "";
        String hStr = "";

        if (min >= 59) {
            int hour = min / 60;
            int temp = min % 60;
            if (hour < 10) {
                if (hour > 0) {
                    hStr = "0" + hour;
                } else {
                    hStr = "00";
                }
            } else {
                hStr = "" + hour;
            }
            if (temp < 10) {
                if (temp > 0) {
                    minStr = "0" + temp;
                } else {
                    minStr = "00";
                }
            } else {
                minStr = "" + temp;
            }
            if (sec < 10) {
                if (sec > 0) {
                    secStr = "0" + sec;
                } else {
                    secStr = "00";
                }
            } else {
                secStr = "" + sec;
            }
            return hStr + ":" + minStr + ":" + secStr;
        } else {
            hStr = "00";
            if (min < 10) {
                if (min > 0) {
                    minStr = "0" + min;
                } else {
                    minStr = "00";
                }
            } else {
                minStr = "" + min;
            }
            if (sec < 10) {
                if (sec > 0) {
                    secStr = "0" + sec;
                } else {
                    secStr = "00";
                }
            } else {
                secStr = "" + sec;
            }
            return hStr + ":" + minStr + ":" + secStr;
        }
    }

}
