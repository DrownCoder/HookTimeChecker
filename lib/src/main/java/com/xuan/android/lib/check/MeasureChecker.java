package com.xuan.android.lib.check;

import android.app.Activity;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


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
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        int widthSize = activity.getResources().getDisplayMetrics().widthPixels;;
        int heightSize = (1 << 30) - 1;
        int widthMode = View.MeasureSpec.EXACTLY;
        int heightMode = View.MeasureSpec.AT_MOST;

        if (params != null) {
            if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                //match或者绝对值，此时mode为EXACTLY或者AT_MOST，因为不知道父的参数，默认给EXACTLY
                if (params.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                    widthSize = params.width;
                }
            } else {
                widthMode = View.MeasureSpec.AT_MOST;
            }
            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                //match或者绝对值，此时mode为EXACTLY或者AT_MOST，因为不知道父的参数，默认给AT_MOST
                if (params.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                    heightSize = params.height;
                }
            }
        }
        int widthSpec = View.MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        rootView.measure(widthSpec, heightSpec);
        rootView.layout(rootView.getLeft(), rootView.getTop(), rootView.getRight(), rootView
                .getBottom());
        rootView.draw(canvas);
        if (TimeLogger.check(CheckerConfig.DRAW_TIME)) {
            TimeLogger.log(LoggerInfoBuilder.create(activity, TimeInfo
                    .STATE.MEASURE, viewHolder, pos));
        }
    }
}
