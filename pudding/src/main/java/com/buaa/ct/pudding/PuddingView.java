package com.buaa.ct.pudding;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PuddingView extends FrameLayout {
    public static final int DISPLAY_TIME = 3000;
    public static final int ANIMATION_DURATION = 500;

    private View root;
    private TextView titleView;
    private TextView subTitleView;
    private ImageView iconView;
    private ProgressBar progressBar;

    private ObjectAnimator animatorEnter;
    private boolean onlyOnce = true;
    private boolean enableIconPulse = true;
    private boolean enableInfiniteDuration;
    private boolean enableProgress;
    private boolean enableVibration;

    public PuddingView(@NonNull Context context) {
        this(context, null);
    }

    public PuddingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuddingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        root = inflate(context, R.layout.layout_pudding, this);
    }

    public void onShow() {

    }

    public void onDismiss() {

    }

    private void initConfig() {
        if (enableIconPulse) {
            iconView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_pulse));
        }
        if (enableProgress) {
            iconView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        } else {
            iconView.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        }
        if (enableVibration) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initConfig();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (onlyOnce) {
            onlyOnce = false;
            animatorEnter = ObjectAnimator.ofFloat(this, "translationY", -this.getMeasuredHeight(), -80f);
            animatorEnter.setInterpolator(new OvershootInterpolator());
            animatorEnter.setDuration(ANIMATION_DURATION);
            animatorEnter.start();
        }
    }

    public void hide(boolean removeNow) {
        if (!isAttachedToWindow()) {
            return;
        }
        final WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (removeNow) {
            windowManager.removeViewImmediate(this);
            return;
        }
        animatorEnter = ObjectAnimator.ofFloat(this, "translationY", -80f, -this.getMeasuredHeight());
        animatorEnter.setInterpolator(new OvershootInterpolator());
        animatorEnter.setDuration(ANIMATION_DURATION);
        animatorEnter.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                windowManager.removeViewImmediate(PuddingView.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorEnter.start();
    }

    public void setBackgroundResource(@DrawableRes int resource) {
        root.setBackgroundResource(resource);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        root.setBackground(drawable);
    }

    public void setTitle(@StringRes int res) {
        setTitle(getContext().getResources().getString(res));
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
    }

    public void setTitleTypeface(Typeface typeface) {
        titleView.setTypeface(typeface);
    }

    public void setTitleStyle(@StyleRes int style) {
        titleView.setTextAppearance(getContext(), style);
    }

    public void setSubTitle(@StringRes int res) {
        setSubTitle(getContext().getResources().getString(res));
    }

    public void setSubTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            subTitleView.setText(title);
        }
    }

    public void setSubTitleTypeface(Typeface typeface) {
        subTitleView.setTypeface(typeface);
    }

    public void setSubTitleStyle(@StyleRes int style) {
        subTitleView.setTextAppearance(getContext(), style);
    }

    public void setIcon(@DrawableRes int iconId) {
        iconView.setImageDrawable(getContext().getResources().getDrawable(iconId));
    }

    public void setIcon(Bitmap bitmap) {
        iconView.setImageBitmap(bitmap);
    }

    public void setIcon(Drawable drawable) {
        iconView.setImageDrawable(drawable);
    }

    public void setIconColorFilter(ColorFilter colorFilter) {
        iconView.setColorFilter(colorFilter);
    }

    public void setIconColorFilter(@ColorRes int color, PorterDuff.Mode mode) {
        iconView.setColorFilter(color, mode);
    }

    public void showIcon(boolean show) {
        if (show) {
            iconView.setVisibility(VISIBLE);
        } else {
            iconView.setVisibility(INVISIBLE);
        }
    }

    public void setOnlyOnce(boolean onlyOnce) {
        this.onlyOnce = onlyOnce;
    }

    public void setEnableIconPulse(boolean enableIconPulse) {
        this.enableIconPulse = enableIconPulse;
    }

    public void setEnableInfiniteDuration(boolean enableInfiniteDuration) {
        this.enableInfiniteDuration = enableInfiniteDuration;
    }

    public void setEnableProgress(boolean enableProgress) {
        this.enableProgress = enableProgress;
    }

    public void setProgressColorRes(@ColorRes int color) {
        setProgressColor(getContext().getResources().getColor(color));
    }

    public void setProgressColor(@ColorInt int color) {
        progressBar.getProgressDrawable().setColorFilter(new LightingColorFilter(-0x1000000, color));
    }

    public void setEnableVibration(boolean enableVibration) {
        this.enableVibration = enableVibration;
    }

    public void setEnableSwipeToDismiss() {
        root.setOnTouchListener(new SwipeDismissTouchListener(this, new DismissCallBack() {
            @Override
            public boolean canDismiss() {
                return true;
            }

            @Override
            public void onDismiss(View view) {
                hide(true);
            }

            @Override
            public void onTouch(View view, boolean touching) {

            }
        }));
    }
}
