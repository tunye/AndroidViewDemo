package com.buaa.ct.core.view;

import android.widget.Toast;

import com.buaa.ct.core.manager.RuntimeManager;


/**
 * 重载后toast 可同时触发
 */
public class CustomToast {
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    private Toast mToast;

    private CustomToast() {

    }

    public static CustomToast getInstance() {
        return SingleInstanceHelper.instance;
    }

    public void showToast(String text) {
        showToast(text, LENGTH_SHORT);
    }

    public void showToast(int resId, int duration) {
        showToast(RuntimeManager.getInstance().getString(resId), duration);
    }

    public void showToast(int resId) {
        showToast(RuntimeManager.getInstance().getString(resId), LENGTH_SHORT);
    }

    public void showToast(String text, int duration) {
        if (mToast != null) {
            mToast.setText(text);
            mToast.setDuration(duration);
        } else {
            mToast = Toast.makeText(RuntimeManager.getInstance().getContext(), text, duration);
        }
        mToast.show();
    }

    private static class SingleInstanceHelper {
        private static CustomToast instance = new CustomToast();
    }
}
