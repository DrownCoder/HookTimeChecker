package com.xuan.android.lib;

/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :the description of this file
 */
public class CheckerConfig {
    public static final String TAG = "TimeChecker";
    //是否开启measure检测
    public static boolean startMeasureTest = true;
    public static final int START_MEASURE_TIME = 15000;
    //检测开始时间
    public static final int START_TIME = 2000;
    //绑定时长
    public static final long BIND_TIME = 11L;
    //绘制时长
    public static final long DRAW_TIME = 16L;
    //重复刷新时间
    public static final long RE_BIND_TIME = 500;
    public static final String BIND = "绑定ViewHolder时间过长";
    public static final String CREATE = "创建ViewHolder时间过长";
    public static final String GET_VIEW = "创建ListView的ItemView耗时过长";
    public static final String MEASURE = "测量绘制过程过长";
    public static final String REBIND = "重复绑定ViewHolder";
    public static final String TOAST = "检测到列表卡顿，具体查看Log信息(" + TAG + ")!";
    public static final String MEASURE_START = "正在进行测量检测，请勿操作";
    public static final String MEASURE_END = "测量检测完毕";
}
