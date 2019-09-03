package com.buaa.ct.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.StrictMode;

import com.buaa.ct.appskin.SkinManager;
import com.buaa.ct.appskin.callback.ISkinChangedListener;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.myapplication.receiver.ChangePropertyBroadcast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10202 on 2016/6/30.
 */
public class MyApplication extends Application {
    private ChangePropertyBroadcast changeProperty;
    private List<Activity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        RuntimeManager.getInstance().initRuntimeManager(this);

        if (shouldInit()) {
            activityList = new ArrayList<>();
            initApplication();
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void initApplication() {
        SkinManager.getInstance().init(this, "TestAppSkin");
        setTheme(GetAppColor.getInstance().getAppTheme());
        SkinManager.getInstance().addChangedListener(new ISkinChangedListener() {
            @Override
            public void onSkinChanged() {
                setTheme(GetAppColor.getInstance().getAppTheme());
            }
        });

        changeProperty = new ChangePropertyBroadcast();
        IntentFilter intentFilter = new IntentFilter(ChangePropertyBroadcast.FLAG);
        registerReceiver(changeProperty, intentFilter);
    }

    public void pushActivity(final Activity activity) {
        activityList.add(activity);
    }

    public void popActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void clearActivityList() {
        Activity activity;
        for (int i = activityList.size() - 1; i > -1; i--) {
            activity = activityList.get(i);
            if (null != activity) {
                activity.finish();
            }
        }
        activityList.clear();
    }
}