package com.xuan.android.lib.check;

import android.app.Activity;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.xuan.android.lib.CheckerConfig;

import static android.view.View.MEASURED_STATE_MASK;

/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :测量检测
 */
public class MeasureChecker {
    /**
     * 强制执行测量流程，测量绘制耗时，可能会导致页面展示异常，不影响功能使用
     */
    public static void checkForceLayout(Activity activity, RecyclerView.ViewHolder viewHolder) {
        View rootView = viewHolder.itemView;
        int pos = viewHolder.getLayoutPosition();
        Canvas canvas = new Canvas();
        TimeLogger.start();
        rootView.forceLayout();
        rootView.measure(rootView.getMeasuredWidthAndState() & MEASURED_STATE_MASK,
                rootView.getMeasuredHeightAndState() & MEASURED_STATE_MASK);
        rootView.layout(rootView.getLeft(), rootView.getTop(), rootView.getRight(),
                rootView.getBottom());
        rootView.draw(canvas);
        if (TimeLogger.check(CheckerConfig.DRAW_TIME)) {
            TimeLogger.log(LoggerInfoBuilder.create(activity, TimeInfo
                    .STATE.MEASURE, viewHolder, pos));
        }
    }
}
