package com.buaa.ct.appskin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.buaa.ct.appskin.SkinManager;

/**
 * Created by ct on 19/9/2.
 */
public class PrefUtils {
    private SharedPreferences sp;

    public PrefUtils() {
        sp = SkinManager.getInstance().getmContext().getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PrefUtils getInstance() {
        return InstanceHelper.instance;
    }

    public String getSuffix() {
        return sp.getString(SkinConfig.KEY_PLUGIN_SUFFIX, "");
    }

    public void clear() {
        sp.edit().clear().apply();
    }

    public void putPluginSuffix(String suffix) {
        sp.edit().putString(SkinConfig.KEY_PLUGIN_SUFFIX, suffix).apply();
    }

    private static class InstanceHelper {
        private static PrefUtils instance = new PrefUtils();
    }
}
