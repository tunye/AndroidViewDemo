package com.buaa.ct.core.view.viewgroup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.buaa.ct.core.manager.RuntimeManager;

/**
 * 软键盘沉浸式出现无法自适应
 * 通过继承FrameLayout解决
 */

public class ImmersiveInsertFrameLayout extends FrameLayout {
    public ImmersiveInsertFrameLayout(Context context) {
        this(context, null);
    }

    public ImmersiveInsertFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFitsSystemWindows(true);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    protected final boolean fitSystemWindows(Rect rect) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            rect.left = 0;
            rect.top = 0;
            rect.right = 0;
        }
        return super.fitSystemWindows(rect);
    }

    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return super.onApplyWindowInsets(windowInsets.replaceSystemWindowInsets(0, 0, 0, windowInsets.getSystemWindowInsetBottom()));
        }
        return windowInsets;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Window window = null;
        if (getContext() instanceof Activity) {
            window = ((Activity) getContext()).getWindow();
        }
        boolean softInputModeInvisible = window != null && window.getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
        if (softInputModeInvisible) {
            int statusBarHeight = RuntimeManager.getInstance().getTopNavBarHeight();
            if (statusBarHeight == 0) {
                Rect outRect = new Rect();
                window.getDecorView().getWindowVisibleDisplayFrame(outRect);
                statusBarHeight = RuntimeManager.getInstance().getWindowHeight() - outRect.height();
            }
            setMargins(this, 0, statusBarHeight, 0, 0);
        }
    }
}
