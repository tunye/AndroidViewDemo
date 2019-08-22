package com.buaa.ct.pudding;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class Pudding implements LifecycleObserver {
    private PuddingView puddingView;
    private WindowManager windowManager;

    public void show() {
        windowManager.addView(puddingView, initLayoutParam());
    }

    public void onDestroy(LifecycleOwner lifecycleOwner) {
//        puddingView.
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    private WindowManager.LayoutParams initLayoutParam() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        layoutParams.gravity = Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不获取焦点，以便于在弹出的时候 下层界面仍然可以进行操作
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR; // 确保你的内容不会被装饰物(如状态栏)掩盖.
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
        return layoutParams;
    }

    public void setActivity(AppCompatActivity activity) {
        puddingView = new PuddingView(activity);
        windowManager = activity.getWindowManager();
        activity.getLifecycle().addObserver(this);
    }

    public PuddingView getPuddingView() {
        return puddingView;
    }
}
