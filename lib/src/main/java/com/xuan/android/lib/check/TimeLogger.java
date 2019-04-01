package com.xuan.android.lib.check;

import android.util.Log;

import static com.xuan.android.lib.CheckerConfig.*;


/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :时间检测逻辑
 */
public class TimeLogger {
    public static long start;

    public static void start() {
        start = System.currentTimeMillis();
    }

    public static void start(long startTime) {
        start = startTime;
    }

    /**
     * 检测耗时
     */
    public static boolean check() {
        long time = System.currentTimeMillis() - start;
        if (time > BIND_TIME) {
            Log.d(TAG, "耗时：" + time);
        }
        return time > BIND_TIME;
    }

    /**
     * 检测耗时
     */
    public static boolean check(long limitTime) {
        long time = System.currentTimeMillis() - start;
        if (time > limitTime) {
            Log.d(TAG, "耗时：" + time);
        }
        return time > limitTime;
    }

    /**
     * log提示
     */
    public static void log(TimeInfo info) {
        Log.d(TAG, info.logInfo);
    }
}

