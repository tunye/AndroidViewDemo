/*
 * Created by chentong1 on 2019.8.20
 */
package com.buaa.ct.pudding.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.buaa.ct.pudding.callback.PuddingCallBack;

public class SwipeDismissListener implements View.OnTouchListener {
    private PuddingView view;
    private PuddingCallBack callBack;

    private int mSlop;
    private int mMinFlingVelocity;
    private int mAnimationTime;
    private int mViewWidth = 1;// 1 and not 0 to prevent dividing by zero
    private VelocityTracker mVelocityTracker;

    private float mDownX, mDownY;
    private boolean mSwiping;
    private float mTranslationX;

    public SwipeDismissListener(PuddingView view, PuddingCallBack callBack) {
        this.view = view;
        this.callBack = callBack;
        init();
    }

    private void init() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(view.getContext());
        mSlop = viewConfiguration.getScaledTouchSlop();
        mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mAnimationTime = view.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        event.offsetLocation(mTranslationX, 0);
        if (mViewWidth < 2) {
            mViewWidth = view.getWidth();
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                mDownY = event.getRawY();
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                float deltaX = event.getRawX() - mDownX;
                float deltaY = event.getRawY() - mDownY;
                if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    mSwiping = true;
                    view.getParent().requestDisallowInterceptTouchEvent(true);

                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    view.onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                }

                if (mSwiping) {
                    mTranslationX = deltaX;
                    view.setTranslationX(deltaX - (deltaX > 0 ? mSlop : -mSlop));
                    view.setAlpha(Math.max(0, Math.min(1, 1 - 2 * Math.abs(deltaX) / mViewWidth)));
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                view.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
                mVelocityTracker.recycle();
                mTranslationX = 0f;
                mDownY = mDownX = 0;
                mSwiping = false;
                if (callBack != null) {
                    callBack.onSwing(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                deltaX = event.getRawX() - mDownX;
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                float velocityXAbs = Math.abs(velocityX);
                float velocityYAbs = Math.abs(mVelocityTracker.getYVelocity());
                boolean dismiss = false;
                boolean dismissRight = false;
                if (Math.abs(deltaX) > mViewWidth / 2f && mSwiping) {
                    dismiss = true;
                    dismissRight = deltaX > 0;
                } else if (mMinFlingVelocity <= velocityXAbs && velocityYAbs < velocityXAbs && mSwiping) {
                    // 速度和方向同向
                    dismiss = (velocityX < 0 == deltaX < 0);
                    dismissRight = velocityX > 0;
                }
                if (dismiss) {
                    if (callBack != null) {
                        callBack.onSwing(true);
                    }
                    view.animate().translationX(dismissRight ? mViewWidth : -mViewWidth).alpha(0)
                            .setDuration(mAnimationTime).setListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            performDismiss();
                        }
                    });
                } else if (mSwiping) {
                    if (callBack != null) {
                        callBack.onSwing(false);
                    }
                    view.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
                }
                mVelocityTracker.recycle();
                mTranslationX = 0f;
                mDownY = mDownX = 0;
                mSwiping = false;
                break;
        }
        return true;
    }

    private void performDismiss() {
        ValueAnimator animator = ValueAnimator.ofInt(view.getHeight(), 1).setDuration(mAnimationTime);
        final WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
        animator.addListener(new SimpleAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (callBack != null) {
                    callBack.dismiss();
                }
                view.hide(true);
                view.setAlpha(1);
                view.setTranslationX(0);
                layoutParams.height = view.getHeight();
                view.setLayoutParams(layoutParams);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (int) animation.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        animator.start();
    }

    public static abstract class SimpleAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
