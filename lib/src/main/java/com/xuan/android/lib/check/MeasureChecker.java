package com.xuan.android.lib.check;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;


import com.xuan.android.lib.CheckerConfig;

import static android.view.View.MEASURED_STATE_MASK;

/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :the description of this file
 */
public class MeasureChecker {
    public static void checkRequestLayout(Activity activity, ViewGroup group) {
        Canvas canvas = new Canvas();
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            TimeLogger.start();
            child.forceLayout();
            child.measure(child.getMeasuredWidthAndState() & MEASURED_STATE_MASK,
                    child.getMeasuredHeightAndState() & MEASURED_STATE_MASK);
            child.layout(child.getLeft(), child.getTop(), child.getRight(),
                    child.getBottom());
            child.draw(canvas);
            if (TimeLogger.check(CheckerConfig.DRAW_TIME)) {
                TimeLogger.log(LoggerInfoBuilder.create(activity, TimeInfo
                        .STATE.MEASURE, child, i));
            }
        }
    }
}
