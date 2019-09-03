package com.buaa.ct.core.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by 10202 on 2015/11/18.
 */
public class SPUtils {
    public static void putBoolean(SharedPreferences preferences, String name, boolean value) {
        preferences.edit().putBoolean(name, value).apply();
    }

    public static void putInt(SharedPreferences preferences, String name, int value) {
        preferences.edit().putInt(name, value).apply();
    }

    public static void putFloat(SharedPreferences preferences, String name, float value) {
        preferences.edit().putFloat(name, value).apply();
    }

    public static void putString(SharedPreferences preferences, String name, @NonNull String value) {
        preferences.edit().putString(name, value).apply();
    }

    public static boolean loadBoolean(SharedPreferences preferences, String key) {
        return preferences.getBoolean(key, false);
    }

    public static boolean loadBoolean(SharedPreferences preferences, String key, boolean defaultBool) {
        return preferences.getBoolean(key, defaultBool);
    }

    public static int loadInt(SharedPreferences preferences, String key) {
        return preferences.getInt(key, 0);
    }

    public static int loadInt(SharedPreferences preferences, String key, int defaultInt) {
        return preferences.getInt(key, defaultInt);
    }

    public static float loadFloat(SharedPreferences preferences, String key) {
        return preferences.getFloat(key, 0);
    }

    public static float loadFloat(SharedPreferences preferences, String key, float defaultInt) {
        return preferences.getFloat(key, defaultInt);
    }

    public static String loadString(SharedPreferences preferences, String key) {
        return preferences.getString(key, "");
    }

    public static String loadString(SharedPreferences preferences, String key, @NonNull String defaultString) {
        return preferences.getString(key, defaultString);
    }

    public static void removeKey(SharedPreferences preferences, String key) {
        preferences.edit().remove(key).apply();
    }

    public static void clear(SharedPreferences preferences) {
        preferences.edit().clear().apply();
    }
}
