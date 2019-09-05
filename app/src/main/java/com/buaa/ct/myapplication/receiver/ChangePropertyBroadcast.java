package com.buaa.ct.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.myapplication.MainActivity;
import com.buaa.ct.myapplication.MyApplication;


/**
 * Created by 10202 on 2017/3/7.
 */

public class ChangePropertyBroadcast extends BroadcastReceiver {
    public static final String FLAG = "changeProperty";

    @Override
    public void onReceive(Context context, Intent intent) {
        ((MyApplication) RuntimeManager.getInstance().getApplication()).clearActivityList();
        Intent target = new Intent();
        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        target.setPackage(RuntimeManager.getInstance().getContext().getPackageName());
        target.setClass(context, MainActivity.class);
        context.startActivity(target);
    }
}