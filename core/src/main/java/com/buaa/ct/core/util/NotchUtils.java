package com.buaa.ct.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.buaa.ct.core.manager.RuntimeManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotchUtils {

    private static final String TAG = "NotchUtils";

    //每次反射取太耗时，这里缓存一下，-1：没取过，0：没刘海，1：有刘海
    private static int oppoNotch = -1;
    private static int vivoNotch = -1;
    private static int huaweiNotch = -1;
    private static int xiaomiNotch = -1;
    private static int samsungNotch = -1;
    private static int notchHeight = -1;

    /**
     * oppo、vivo、华为、小米、三星有没有刘海屏
     */
    public static boolean hasNotchScreen() {
        return vivoHasNotch() || oppoHasNotch() || xiaomiHasNotch() || huaweiHasNotch() || samsungHasNotch();
    }

    /**
     * oppo、vivo、华为、小米有没有刘海屏
     */
    public static boolean internalHasNotchScreen(Context context) {
        return vivoHasNotch() || oppoHasNotch() || xiaomiHasNotch() || huaweiHasNotch();
    }


    /**
     * 华为、小米使用刘海屏
     */
    public static void hmEnableNotchScreen(Window window) {
        if (huaweiHasNotch()) {
            huaweiEnableNotch(window);
        } else if (xiaomiHasNotch()) {
            xiaomiEnableNotch(window);
        }
    }

    /**
     * 华为、小米不使用刘海屏
     */
    public static void hmDisableNotchScreen(Window window) {
        if (huaweiHasNotch()) {
            huaweiDisableNotch(window);
        } else if (xiaomiHasNotch()) {
            xiaomiDisableNotch(window);
        }
    }

    public static int getNotchOffset() {
        if (notchHeight == -1) {
            int offset = 0;
            if (huaweiHasNotch()) {//华为使用其提供的方法
                offset = getHuaweiNotchSize()[1];
            }
            if (offset == 0) {//o、v、小米、三星使用状态栏
                offset = RuntimeManager.getInstance().getTopNavBarHeight();
            }
            notchHeight = offset;
        }
        return notchHeight;
    }

    public static boolean samsungHasNotch() {
        if (samsungNotch == -1) {
            boolean result = false;
            try {
                final Resources res = RuntimeManager.getInstance().getResource();
                final int resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android");
                final String spec = resId > 0 ? res.getString(resId) : null;
                result = spec != null && !TextUtils.isEmpty(spec);
            } catch (Exception ignored) {
            }
            samsungNotch = result ? 1 : 0;
            return result;
        } else {
            return samsungNotch == 1;
        }
    }

    /**
     * oppo有没有刘海屏
     * 仅测试过O系统
     */
    public static boolean oppoHasNotch() {
        if (oppoNotch == -1) {
            oppoNotch = RuntimeManager.getInstance().getContext().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism") ? 1 : 0;
        }
        return oppoNotch == 1;
    }

    /**
     * vivo有没有刘海屏
     * 仅测试过O系统
     */
    public static boolean vivoHasNotch() {
        if (vivoNotch == -1) {
            final int HAS_NOTCH = 0x00000020;
            boolean hasNotch = false;
            try {
                Class<?> FtFeature = Class.forName("android.util.FtFeature");
                Method method = FtFeature.getMethod("isFeatureSupport", int.class);
                if (method != null) {
                    hasNotch = (Boolean) method.invoke(null, HAS_NOTCH);
                }
            } catch (Exception e) {
            }
            vivoNotch = hasNotch ? 1 : 0;
        }
        return vivoNotch == 1;
    }

    /**
     * 华为有没有刘海屏
     * 仅测试过O系统
     */
    public static boolean huaweiHasNotch() {
        if (huaweiNotch == -1) {
            boolean ret = false;
            try {
                ClassLoader cl = RuntimeManager.getInstance().getContext().getClassLoader();
                Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
                Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
                ret = (boolean) get.invoke(HwNotchSizeUtil);
            } catch (Exception e) {
            }
            huaweiNotch = ret ? 1 : 0;
        }
        return huaweiNotch == 1;
    }

    /**
     * 华为刘海屏高度
     * 仅测试过O系统
     */
    public static int[] getHuaweiNotchSize() {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = RuntimeManager.getInstance().getContext().getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
        } finally {
            return ret;
        }
    }


    /**
     * 华为刘海屏手机使用刘海区
     * 仅测试过O系统
     */
    public static void huaweiEnableNotch(Window window) {
        final int FLAG_NOTCH_SUPPORT = 0x00010000;//华为刘海屏全屏显示FLAG
        try {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("addHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (Exception e) {
        }
    }

    /**
     * 华为刘海屏手机不使用刘海区
     */
    public static void huaweiDisableNotch(Window window) {
        final int FLAG_NOTCH_SUPPORT = 0x00010000;//华为刘海屏全屏显示FLAG
        try {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("clearHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (Exception e) {
        }
    }

    /**
     * 小米手机有没有刘海屏
     * O和P系统均可用
     */
    public static boolean xiaomiHasNotch() {
        if (xiaomiNotch == -1) {
            int ret = 0;
            try {
                Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = {String.class, int.class};
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                Object[] params = {"ro.miui.notch", 0};
                ret = (Integer) getInt.invoke(SystemProperties, params);
            } catch (Exception e) {
            }
            xiaomiNotch = (ret == 1) ? 1 : 0;
        }
        return xiaomiNotch == 1;
    }

    /**
     * 小米使用刘海区
     * O和P系统均可用
     */
    public static void xiaomiEnableNotch(Window window) {
        if (Build.VERSION.SDK_INT >= 28) {//P系统
            WindowManager.LayoutParams params = window.getAttributes();
            try {
                Field field = params.getClass().getField("layoutInDisplayCutoutMode");
                field.setInt(params, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.setAttributes(params);
        } else {//O系统
            int flag = 0x00000100 | 0x00000200;
            try {
                Method method = Window.class.getMethod("addExtraFlags", int.class);
                method.invoke(window, flag);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 小米不使用刘海区
     * O和P系统均可用
     */
    public static void xiaomiDisableNotch(Window window) {
        if (Build.VERSION.SDK_INT >= 28) {//P系统
            WindowManager.LayoutParams params = window.getAttributes();
            try {
                Field field = params.getClass().getField("layoutInDisplayCutoutMode");
                field.setInt(params, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.setAttributes(params);
        } else {//O系统
            int flag = 0x00000100 | 0x00000200;
            try {
                Method method = Window.class.getMethod("clearExtraFlags", int.class);
                method.invoke(window, flag);
            } catch (Exception e) {
            }
        }
    }
}
