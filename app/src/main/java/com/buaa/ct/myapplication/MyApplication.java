package com.buaa.ct.myapplication;

import android.app.Application;

import com.buaa.ct.appskin.SkinManager;

/**
 * Created by 10202 on 2016/6/30.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this, "Skin");
    }
}