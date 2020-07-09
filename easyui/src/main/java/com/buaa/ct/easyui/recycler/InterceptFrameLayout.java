package com.buaa.ct.easyui.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class InterceptFrameLayout extends FrameLayout {

    private boolean mIntercept = true;

    public InterceptFrameLayout(Context context) {
        super(context);
    }

    public InterceptFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIntercept) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    public void setIntercept(boolean intercept) {
        this.mIntercept = intercept;
    }
}
