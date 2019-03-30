package com.xuan.android.lib.check;

/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :信息
 */
public class TimeInfo {
    public enum STATE {
        CREATE, BIND, GET_VIEW, MEASURE, REBIND
    }

    public STATE state;

    public String logInfo;
}
