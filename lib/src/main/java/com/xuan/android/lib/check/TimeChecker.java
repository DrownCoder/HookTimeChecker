package com.xuan.android.lib.check;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xuan.android.lib.CheckerConfig;
import com.xuan.android.lib.hook.HookListAdapter;
import com.xuan.android.lib.hook.HookRcyAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :列表类型的耗时检测，支持RecyclerView,ListView
 */
public class TimeChecker {
    private static volatile TimeChecker checker;

    private Set<ViewGroup> measureCheck;
    private WeakReference<Activity> wkAc;
    private Handler handler = new Handler();


    public static TimeChecker init(Application application) {
        if (checker == null) {
            synchronized (TimeChecker.class) {
                if (checker == null) {
                    checker = new TimeChecker(application);
                }
            }
        }
        return checker;
    }

    public TimeChecker(Application application) {
        measureCheck = new LinkedHashSet<>();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks
                () {


            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                wkAc = new WeakReference<>(activity);
                handler.postDelayed(runnable, CheckerConfig.START_TIME);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (!measureCheck.isEmpty()) {
                    for (ViewGroup group : measureCheck) {
                        if (group instanceof RecyclerView || group instanceof ListView) {
                            MeasureChecker.checkRequestLayout(activity, group);
                        }
                    }
                    measureCheck.clear();
                    wkAc.clear();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                measureCheck.clear();
                wkAc.clear();
            }
        });
    }

    private void check() {
        Activity activity = wkAc.get();
        if (activity == null) {
            return;
        }

        List<View> views = getAllChildViews(activity.getWindow().getDecorView());
        for (View item : views) {
            if (item instanceof RecyclerView) {
                RecyclerView.Adapter adapter = ((RecyclerView) item).getAdapter();
                if (adapter != null) {
                    HookRcyAdapter hookAdapter = new HookRcyAdapter(activity, adapter);
                    ((RecyclerView) item).setAdapter(hookAdapter);
                    measureCheck.add((ViewGroup) item);
                }
            } else if (item instanceof ListView) {
                ListAdapter adapter = ((ListView) item).getAdapter();
                if (adapter != null) {
                    HookListAdapter hookAdapter = new HookListAdapter(activity, adapter);
                    ((ListView) item).setAdapter(hookAdapter);
                }
                measureCheck.add((ViewGroup) item);
            }
        }
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            check();
        }
    };


    private static List<View> getAllChildViews(View view) {
        List<View> childViews = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View child = vp.getChildAt(i);
                childViews.add(child);
                //再次 调用本身（递归）
                childViews.addAll(getAllChildViews(child));
            }
        }
        return childViews;
    }

}
