package com.buaa.ct.core.listener;

import android.view.View;

public abstract class INoDoubleClick implements View.OnClickListener {
    public static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onClick(View v) {
        if (isFastDoubleClick()) {
            return;
        }
        activeClick(v);
    }

    public abstract void activeClick(View v);
}