package com.buaa.ct.appskin;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.buaa.ct.appskin.attr.SkinView;
import com.buaa.ct.appskin.callback.ISkinChangedListener;
import com.buaa.ct.appskin.utils.PrefUtils;
import com.buaa.ct.appskin.utils.SkinConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ct on 19/9/2.
 */
public class SkinManager {
    private Context mContext;

    /**
     * 换肤资源后缀
     */
    private String mSuffix = "";


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new ArrayMap<>();
    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<>();

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        return SingletonHolder.sInstance;
    }

    public void init(Application application, String prefName) {
        mContext = application.getApplicationContext();
        SkinConfig.PREF_NAME = prefName;

        mSuffix = PrefUtils.getInstance().getSuffix();
    }

    public Context getmContext() {
        return mContext;
    }

    public void removeAnySkin() {
        clearPluginInfo();
        notifyChangedListeners();
    }

    public boolean needChangeSkin() {
        return !TextUtils.isEmpty(mSuffix);
    }

    public ResourceManager getResourceManager() {
        ResourceManager mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        return mResourceManager;
    }

    /**
     * 应用内换肤，传入资源区别的后缀
     *
     * @param suffix
     */
    public void changeSkin(String suffix) {
        clearPluginInfo();//clear before
        mSuffix = suffix;
        PrefUtils.getInstance().putPluginSuffix(suffix);
        notifyChangedListeners();
    }

    public String getCurrSkin() {
        return PrefUtils.getInstance().getSuffix();
    }

    private void clearPluginInfo() {
        mSuffix = null;
        PrefUtils.getInstance().clear();
    }

    public void addSkinView(ISkinChangedListener listener, List<SkinView> skinViews) {
        mSkinViewMaps.put(listener, skinViews);
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener) {
        return mSkinViewMaps.get(listener);
    }

    public void apply(ISkinChangedListener listener) {
        List<SkinView> skinViews = getSkinViews(listener);

        if (skinViews == null) return;
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public void addChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.add(listener);
    }

    public void removeChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }

    public void notifyChangedListeners() {
        for (ISkinChangedListener listener : mSkinChangedListeners) {
            listener.onSkinChanged();
        }
    }

    private static class SingletonHolder {
        private static SkinManager sInstance = new SkinManager();
    }

}
