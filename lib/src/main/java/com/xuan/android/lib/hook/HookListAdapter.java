package com.xuan.android.lib.hook;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.xuan.android.lib.check.LoggerInfoBuilder;
import com.xuan.android.lib.check.TimeInfo;
import com.xuan.android.lib.check.TimeLogger;


/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :ListView 的hook过程
 */
public class HookListAdapter implements ListAdapter {
    private Context context;
    private ListAdapter hookAdapter;

    public HookListAdapter(Context context, ListAdapter hookAdapter) {
        this.context = context;
        this.hookAdapter = hookAdapter;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return hookAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return hookAdapter.isEnabled(position);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        hookAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        hookAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return hookAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return hookAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return hookAdapter.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return hookAdapter.hasStableIds();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeLogger.start();
        View view = hookAdapter.getView(position, convertView, parent);
        if (TimeLogger.check()) {
            TimeLogger.log(LoggerInfoBuilder.create(context, TimeInfo.STATE.GET_VIEW, this,
                    position));
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return hookAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return hookAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return hookAdapter.isEmpty();
    }
}
