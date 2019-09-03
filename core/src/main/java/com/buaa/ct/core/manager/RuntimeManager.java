package com.buaa.ct.core.manager;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;


public class RuntimeManager {
    private static final String NAVIGATION = "navigationBarBackground";
    private Application application;
    private DisplayMetrics displayMetrics;
    private Context context;

    private RuntimeManager() {
    }

    public static RuntimeManager getInstance() {
        return InstanceHelper.instance;
    }

    public void initRuntimeManager(Application application) {
        this.application = application;
        context = application.getApplicationContext();
        displayMetrics = application.getResources().getDisplayMetrics();
    }

    public Application getApplication() {
        return application;
    }

    public Context getContext() {
        return context;
    }

    public Resources getResource() {
        return application.getResources();
    }

    public String getString(int resourcesID) {
        return getContext().getString(resourcesID);
    }

    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public int getWindowWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public int getWindowHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);
        return p.x;
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Point p = new Point();
        wm.getDefaultDisplay().getRealSize(p);
        return p.y;
    }

    public int getScreenHeightNoNav() {
        return getScreenHeight() - getBottomNavBarHeight();
    }

    public int getBottomNavBarHeight() {
        if (!hasNavBar()) {
            return 0;
        }
        Resources resources = getResource();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public int getTopNavBarHeight() {
        int resourceId = getResource().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResource().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private boolean hasNavBar() {
        WindowManager wm = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            return true;
        }
    }

    public int dip2px(float dpValue) {
        float scale = getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public int px2dip(float pxValue) {
        float scale = getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public int px2sp(float pxValue) {
        float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5F);
    }

    public int sp2px(float spValue) {
        float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }

    private static class InstanceHelper {
        private static RuntimeManager instance = new RuntimeManager();
    }
}
