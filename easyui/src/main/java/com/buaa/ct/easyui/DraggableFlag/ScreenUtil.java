package com.buaa.ct.easyui.DraggableFlag;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by 琪 on 2015/8/26.
 */
public class ScreenUtil {
    public static final String TAG = ScreenUtil.class.getSimpleName();

    public ScreenUtil() {
    }

    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5F);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }
}
