/*
 * Created by chentong1 on 2019.8.20
 */
package com.buaa.ct.pudding.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

public class ScreenUtil {
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static WindowManager.LayoutParams initLayoutParam() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不获取焦点，以便于在弹出的时候 下层界面仍然可以进行操作
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR; // 确保你的内容不会被装饰物(如状态栏)掩盖.
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
        return layoutParams;
    }
}
