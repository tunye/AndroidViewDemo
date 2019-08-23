/*
 * Created by chentong1 on 2019.8.20
 */
package com.buaa.ct.pudding.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buaa.ct.pudding.R;
import com.buaa.ct.pudding.util.PuddingBuilder;
import com.buaa.ct.pudding.util.ScreenUtil;

public class PuddingView extends FrameLayout {
    public static final int DISPLAY_TIME = 3000;
    public static final int ANIMATION_DURATION = 500;

    private WindowManager windowManager;
    private PuddingBuilder puddingBuilder;
    private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

    private View root;
    private TextView titleView;
    private TextView subTitleView;
    private ImageView iconView;
    private ProgressBar progressBar;
    private LinearLayout buttonContainer;
    private ObjectAnimator animatorEnter;

    public PuddingView(@NonNull Context context) {
        this(context, null);
    }

    public PuddingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuddingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_pudding, this);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        root = findViewById(R.id.body);
        root.setPadding(root.getPaddingStart(), root.getPaddingTop() + ScreenUtil.getStatusBarHeight(getContext()), root.getPaddingEnd(), root.getPaddingBottom());
        titleView = findViewById(R.id.text);
        subTitleView = findViewById(R.id.subText);
        iconView = findViewById(R.id.icon);
        buttonContainer = findViewById(R.id.buttonContainer);
        progressBar = findViewById(R.id.progress);
    }

    private void initConfig() {
        if (puddingBuilder.isEnableIconAnimation()) {
            iconView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_pulse));
        }
        if (puddingBuilder.isEnableProgress()) {
            iconView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        } else {
            iconView.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        }
        if (puddingBuilder.isEnableVibration()) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
        if (puddingBuilder.isEnableSwipeDismiss()) {
            root.setOnTouchListener(new SwipeDismissListener(this, puddingBuilder.getPuddingCallBack()));
        }

        if (puddingBuilder.getBackgroundColor() != -1) {
            root.setBackgroundColor(puddingBuilder.getBackgroundColor());
        }
        if (puddingBuilder.getTitleColor() != -1) {
            titleView.setTextColor(puddingBuilder.getTitleColor());
        }
        if (puddingBuilder.getSubTitleColor() != -1) {
            subTitleView.setTextColor(puddingBuilder.getSubTitleColor());
        }
        if (puddingBuilder.getProgressColor() != -1) {
            progressBar.getProgressDrawable().setColorFilter(new LightingColorFilter(-0x1000000, puddingBuilder.getProgressColor()));
        }
        if (puddingBuilder.getIconColor() != -1) {
            iconView.setColorFilter(puddingBuilder.getIconColor());
        }

        if (puddingBuilder.getProgressFilter() != null) {
            progressBar.getProgressDrawable().setColorFilter(puddingBuilder.getProgressFilter());
        }
        if (puddingBuilder.getIconFilter() != null) {
            iconView.setColorFilter(puddingBuilder.getIconFilter());
        }

        if (puddingBuilder.getIconDrawable() != null) {
            iconView.setImageDrawable(puddingBuilder.getIconDrawable());
        }
        if (puddingBuilder.getBackgroundDrawable() != null) {
            root.setBackground(puddingBuilder.getBackgroundDrawable());
        }

        if (puddingBuilder.getIconDrawableRes() != -1) {
            iconView.setImageResource(puddingBuilder.getIconDrawableRes());
        }
        if (puddingBuilder.getBackgroundDrawable() != null) {
            root.setBackgroundResource(puddingBuilder.getBackgroundDrawableRes());
        }

        if (puddingBuilder.getTitleTypeface() != null) {
            titleView.setTypeface(puddingBuilder.getTitleTypeface());
        }
        if (puddingBuilder.getSubTitleTypeface() != null) {
            subTitleView.setTypeface(puddingBuilder.getSubTitleTypeface());
        }

        if (!TextUtils.isEmpty(puddingBuilder.getTitleText())) {
            titleView.setText(puddingBuilder.getTitleText());
        }
        if (!TextUtils.isEmpty(puddingBuilder.getSubTitleText())) {
            subTitleView.setText(puddingBuilder.getSubTitleText());
            subTitleView.setVisibility(VISIBLE);
        }

        if (!TextUtils.isEmpty(puddingBuilder.getPositiveText())) {
            Button button = new AppCompatButton(new ContextThemeWrapper(getContext(),puddingBuilder.getPositiveStyle()), null, puddingBuilder.getPositiveStyle());
            button.setText(puddingBuilder.getPositiveText());
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide(true);
                    puddingBuilder.getPositiveListener().onClick(v);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (!TextUtils.isEmpty(puddingBuilder.getNegativeText())) {
                layoutParams.setMarginEnd((int) (16 * getContext().getResources().getDisplayMetrics().density));
            }
            buttonContainer.addView(button, 0, layoutParams);
        }
        if (!TextUtils.isEmpty(puddingBuilder.getNegativeText())) {
            Button button = new AppCompatButton(new ContextThemeWrapper(getContext(),puddingBuilder.getNegativeStyle()), null, puddingBuilder.getNegativeStyle());
            button.setText(puddingBuilder.getNegativeText());
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide(false);
                    puddingBuilder.getNegativeListener().onClick(v);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonContainer.addView(button, 1, layoutParams);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initConfig();
        if (puddingBuilder.getPuddingCallBack() != null) {
            puddingBuilder.getPuddingCallBack().show();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        animatorEnter = ObjectAnimator.ofFloat(this, "translationY", -this.getMeasuredHeight(), 0);
        animatorEnter.setInterpolator(interpolator);
        animatorEnter.setDuration(ANIMATION_DURATION);
        animatorEnter.start();
    }

    public void setPuddingBuilder(PuddingBuilder puddingBuilder) {
        this.puddingBuilder = puddingBuilder;
    }

    public void show() {
        windowManager.addView(this, ScreenUtil.initLayoutParam());
        if (!puddingBuilder.isEnableInfiniteDuration()) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    hide(false);
                }
            }, DISPLAY_TIME);
        }
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(false);
            }
        });
    }

    public void hide(boolean removeNow) {
        if (!isAttachedToWindow()) {
            return;
        }
        if (removeNow) {
            windowManager.removeViewImmediate(this);
            return;
        }
        animatorEnter = ObjectAnimator.ofFloat(this, "translationY", 0, -this.getMeasuredHeight());
        animatorEnter.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorEnter.setDuration(ANIMATION_DURATION);
        animatorEnter.addListener(new SwipeDismissListener.SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                windowManager.removeViewImmediate(PuddingView.this);
            }
        });
        animatorEnter.start();
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
            subTitleView.setVisibility(VISIBLE);
        }
    }

    public void setSubTitleTypeface(Typeface typeface) {
        subTitleView.setTypeface(typeface);
    }

    public void setSubTitleStyle(@StyleRes int style) {
        subTitleView.setTextAppearance(getContext(), style);
    }
}
