package com.xuan.android.lib.hook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.xuan.android.lib.CheckerConfig;
import com.xuan.android.lib.TimeChecker;
import com.xuan.android.lib.check.LoggerInfoBuilder;
import com.xuan.android.lib.check.TimeInfo;
import com.xuan.android.lib.check.TimeLogger;

import java.util.HashMap;


/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :hook RecyclerView Adapter的绑定流程
 */
public class HookRcyAdapter extends RecyclerView.Adapter {
    private RecyclerView.Adapter hookAdapter;
    private Context context;
    private HashMap<Integer, Long> reBindError;

    public HookRcyAdapter(Context context, RecyclerView.Adapter hookAdapter) {
        this.context = context;
        this.hookAdapter = hookAdapter;
        reBindError = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        return hookAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return hookAdapter.getItemId(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TimeLogger.start();
        RecyclerView.ViewHolder holder = hookAdapter.onCreateViewHolder(viewGroup, i);
        if (TimeLogger.check()) {
            TimeLogger.log(LoggerInfoBuilder.create(context, TimeInfo.STATE
                    .CREATE, holder, i));
        }
        TimeChecker.getInstance().putMeasureTest(holder.getClass(), holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        long startTime = System.currentTimeMillis();
        if (reBindError.get(i) != null) {
            long lastBindTime = reBindError.get(i);
            if ((startTime - lastBindTime) < CheckerConfig.RE_BIND_TIME) {
                //重复绑定
                TimeLogger.log(LoggerInfoBuilder.create(context, TimeInfo.STATE
                        .REBIND, viewHolder, i));
            }
        }
        reBindError.put(i, startTime);
        TimeLogger.start(startTime);
        hookAdapter.onBindViewHolder(viewHolder, i);
        if (TimeLogger.check()) {
            TimeLogger.log(LoggerInfoBuilder.create(context, TimeInfo.STATE
                    .BIND, viewHolder, i));
        }
    }

    @Override
    public int getItemCount() {
        return hookAdapter.getItemCount();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        hookAdapter.onViewDetachedFromWindow(holder);
        reBindError.remove(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        hookAdapter.onViewRecycled(holder);
        reBindError.remove(holder);
    }
}
