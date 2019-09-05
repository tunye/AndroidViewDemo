package com.buaa.ct.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.util.DisplayMetrics;

import com.buaa.ct.core.manager.ImmersiveManager;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.SPUtils;
import com.buaa.ct.myapplication.activity.SettingActivity;

import java.util.Locale;

/**
 * Created by 10202 on 2015/10/9.
 */
public class ChangePropery {
    public static void setAppConfig(Activity activity) {
        ChangePropery.updateNightMode(activity, SPUtils.loadInt(ConfigManager.getInstance().getPreferences(), SettingActivity.NIGHT, 0) == 1);
        ChangePropery.updateLanguageMode(activity, SPUtils.loadInt(ConfigManager.getInstance().getPreferences(), SettingActivity.LANGUAGE, 0));
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ImmersiveManager.getInstance().updateImmersiveStatus(activity);
    }

    public static void updateNightMode(boolean on) {
        Resources resources = RuntimeManager.getInstance().getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        resources.updateConfiguration(config, dm);
    }

    public static void updateNightMode(Context context, boolean on) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        resources.updateConfiguration(config, dm);
    }

    public static void updateLanguageMode(int languageType) {
        Resources resources = RuntimeManager.getInstance().getContext().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        changeLanguage(languageType, config);
        resources.updateConfiguration(config, dm);
    }

    public static void updateLanguageMode(Context context, int languageType) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        changeLanguage(languageType, config);
        resources.updateConfiguration(config, dm);
    }

    private static void changeLanguage(int languageType, Configuration config) {
        switch (languageType) {
            case 0://跟随系统
                config.setLocale(Locale.SIMPLIFIED_CHINESE);
                break;
            case 1://英文
                config.setLocale(Locale.ENGLISH);
                break;
        }
    }
}
