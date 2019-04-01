package com.xuan.android.lib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xuan.android.lib.check.MeasureChecker;
import com.xuan.android.lib.hook.HookListAdapter;
import com.xuan.android.lib.hook.HookRcyAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author : xuan.
 * Date : 2019/3/30.
 * Description :列表类型的耗时检测，支持RecyclerView,ListView
 */
public class TimeChecker {
    private static volatile TimeChecker checker;

    private WeakReference<Activity> wkAc;
    //测量检测
    private HashMap<Class<?>, RecyclerView.ViewHolder> measureTime;
    //重复检测校验，检测过的，就不再检测
    private HashMap<Class<?>, Boolean> measureCheck;
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

    private TimeChecker(Application application) {
        measureTime = new HashMap<>();
        measureCheck = new HashMap<>();
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
                if (CheckerConfig.startMeasureTest) {
                    handler.postDelayed(measureTest, CheckerConfig.START_MEASURE_TIME);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                measureTime.clear();
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public static TimeChecker getInstance() {
        return checker;
    }

    public void putMeasureTest(Class<?> clazz, RecyclerView.ViewHolder holder) {
        if (CheckerConfig.startMeasureTest) {
            measureTime.put(clazz, holder);
        }
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
                }
            } else if (item instanceof ListView) {
                ListAdapter adapter = ((ListView) item).getAdapter();
                if (adapter != null) {
                    HookListAdapter hookAdapter = new HookListAdapter(activity, adapter);
                    ((ListView) item).setAdapter(hookAdapter);
                }
            }
        }
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            check();
        }
    };

    public Runnable measureTest = new Runnable() {
        @Override
        public void run() {
            if (CheckerConfig.startMeasureTest) {
                Activity activity = wkAc.get();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                Iterator<Map.Entry<Class<?>, RecyclerView.ViewHolder>> entries = measureTime
                        .entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<Class<?>, RecyclerView.ViewHolder> entry = entries.next();
                    if (measureCheck.get(entry.getKey()) == null ||
                            !measureCheck.get(entry.getKey())) {
                        MeasureChecker.checkForceLayout(activity, entry.getValue());
                        measureCheck.put(entry.getKey(), true);
                    } else {
                        entries.remove();
                    }
                }
                handler.postDelayed(measureTest, CheckerConfig.START_MEASURE_TIME);
            }
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
