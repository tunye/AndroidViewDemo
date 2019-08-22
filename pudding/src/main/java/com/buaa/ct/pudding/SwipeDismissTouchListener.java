package com.buaa.ct.pudding;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

public class SwipeDismissTouchListener implements View.OnTouchListener {
    private View view;
    private DismissCallBack callBack;

    private int mSlop;
    private int mMinFlingVelocity;
    private int mAnimationTime;
    private int mViewWidth = 1;// 1 and not 0 to prevent dividing by zero

    private float mDownX, mDownY;
    private boolean mSwiping;
    private int mSwipingSlop;
    private VelocityTracker mVelocityTracker;
    private float mTranslationX;

    public SwipeDismissTouchListener(View view, DismissCallBack callBack) {
        this.view = view;
        this.callBack = callBack;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(view.getContext());
        mSlop = viewConfiguration.getScaledTouchSlop();
        mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 16;
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
                if (callBack.canDismiss()) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                }
                callBack.onTouch(view, true);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                float deltaX = event.getRawX() - mDownX;
                float deltaY = event.getRawY() - mDownY;
                if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    mSwiping = true;
                    mSwipingSlop = deltaX > 0 ? mSlop : -mSlop;
                    view.getParent().requestDisallowInterceptTouchEvent(true);

                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    view.onTouchEvent(cancelEvent);
                    cancelEvent.recycle();
                }

                if (mSwiping) {
                    mTranslationX = deltaX;
                    view.setTranslationX(deltaX - mSwipingSlop);
                    view.setAlpha(Math.max(0, Math.min(1, 1 - 2 * Math.abs(deltaX) / mViewWidth)));
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                view.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
                mVelocityTracker.recycle();
                mTranslationX = 0f;
                mDownY = mDownX = 0;
                mSwiping = false;
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
                if (Math.abs(deltaX) > mViewWidth / 2 && mSwiping) {
                    dismiss = true;
                    dismissRight = deltaX > 0;
                } else if (mMinFlingVelocity <= velocityXAbs && velocityYAbs < velocityXAbs && mSwiping) {
                    // 速度和方向同向
                    dismiss = (velocityX < 0 == deltaX < 0);
                    dismissRight = velocityX > 0;
                }
                if (dismiss) {
                    view.animate().translationX(dismissRight ? mViewWidth : -mViewWidth).alpha(0)
                            .setDuration(mAnimationTime).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            performDismiss();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                } else if (mSwiping) {
                    // 等效取消
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
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                callBack.onDismiss(view);
                view.setAlpha(1);
                view.setTranslationX(0);
                layoutParams.height = view.getHeight();
                view.setLayoutParams(layoutParams);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

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
}
