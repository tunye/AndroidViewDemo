package com.buaa.ct.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;

import com.buaa.ct.core.manager.RuntimeManager;

public class ConfigManager {
    private static final String CONFIG_NAME = "TestApp";
    private SharedPreferences preferences;

    private ConfigManager() {
        preferences = RuntimeManager.getInstance().getContext().getSharedPreferences(CONFIG_NAME, Activity.MODE_PRIVATE);
    }

    public static ConfigManager getInstance() {
        return SingleInstanceHelper.instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    private static class SingleInstanceHelper {
        private static ConfigManager instance = new ConfigManager();
    }
}
