package com.buaa.ct.imageselector.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dee on 15/11/24.
 */
public class PreviewViewPager extends ViewPager {
    private boolean lock;
    public PreviewViewPager(Context context) {
        super(context);
    }

    public PreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (lock) {
                return false;
            } else {
                return super.onTouchEvent(ev);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (lock) {
                return false;
            } else {
                return super.onInterceptTouchEvent(ev);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
