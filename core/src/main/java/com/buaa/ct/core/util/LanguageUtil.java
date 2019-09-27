package com.buaa.ct.core.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageUtil {
    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            if (locale.getCountry().equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getCountry()) &&
                    locale.getLanguage().equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
                return Locale.SIMPLIFIED_CHINESE;
            } else {
                return locale;
            }
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }


    public static Context updateLanguage(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }
}
