package com.xuan.android.lib.check;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;

import static com.xuan.android.lib.CheckerConfig.*;


/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :信息构造者
 */
public class LoggerInfoBuilder {
    public static TimeInfo create(Context context, TimeInfo.STATE state, RecyclerView.ViewHolder
            holder, int pos) {
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.state = state;
        timeInfo.logInfo = createBaseInfo(context, state)
                .append("ViewHolder：").append(holder.getClass().getSimpleName()).append("\n")
                .append("ViewType：").append(holder.getItemViewType()).append("\n")
                .append("Position：").append(pos).toString();
        return timeInfo;
    }

    @NonNull
    private static String createReason(TimeInfo.STATE state) {
        String tip = null;
        switch (state) {
            case BIND:
                tip = BIND;
                break;
            case CREATE:
                tip = CREATE;
                break;
            case GET_VIEW:
                tip = GET_VIEW;
                break;
            case MEASURE:
                tip = MEASURE;
                break;
            case REBIND:
                tip = REBIND;
                break;
        }
        return tip;
    }

    public static TimeInfo create(Context context, TimeInfo.STATE state, ListAdapter adapter, int
            pos) {
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.state = state;
        timeInfo.logInfo =
                createBaseInfo(context, state)
                        .append("ViewType：").append(adapter.getItemViewType(pos)).append("\n")
                        .append("Position：").append(pos).toString();
        return timeInfo;
    }

    public static TimeInfo create(Context context, TimeInfo.STATE state, View view, int pos) {
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.state = state;
        timeInfo.logInfo =
                createBaseInfo(context, state)
                        .append("ViewClass：").append(view.getClass().getSimpleName()).append("\n")
                        .append("Position：").append(pos).toString();
        return timeInfo;
    }

    public static StringBuilder createBaseInfo(Context context, TimeInfo.STATE state) {
        String tip = createReason(state);
        //Toast.makeText(context, TOAST, Toast.LENGTH_SHORT).show();
        StringBuilder builder = new StringBuilder(100);
        builder.append("\n")
                .append("页面信息：").append(context.getClass().getSimpleName()).append("\n")
                .append("卡断原因：").append(tip).append("\n");
        return builder;
    }
}

