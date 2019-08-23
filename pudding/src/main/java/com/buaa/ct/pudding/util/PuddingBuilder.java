/*
 * Created by chentong1 on 2019.8.20
 */
package com.buaa.ct.pudding.util;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.buaa.ct.pudding.callback.PuddingCallBack;
import com.buaa.ct.pudding.view.PuddingView;

import java.util.HashMap;
import java.util.Map;

public class PuddingBuilder implements LifecycleObserver {
    private static Map<String, PuddingView> puddingMap = new HashMap<>();
    private PuddingCallBack puddingCallBack;

    private boolean enableIconAnimation;
    private boolean enableInfiniteDuration;
    private boolean enableProgress;
    private boolean enableVibration;
    private boolean enableSwipeDismiss;

    private @ColorInt
    int backgroundColor = -1;
    private @ColorInt
    int titleColor = -1;
    private @ColorInt
    int subTitleColor = -1;
    private @ColorInt
    int progressColor = -1;
    private @ColorInt
    int iconColor = -1;

    private @DrawableRes
    int backgroundDrawableRes = -1;
    private @DrawableRes
    int iconDrawableRes = -1;

    private Drawable backgroundDrawable;
    private Drawable iconDrawable;

    private ColorFilter iconFilter;
    private ColorFilter progressFilter;

    private String titleText;
    private String subTitleText;

    private Typeface titleTypeface;
    private Typeface subTitleTypeface;

    private String positiveText;
    private @StyleRes
    int positiveStyle;
    private View.OnClickListener positiveListener;

    private String negativeText;
    private @StyleRes
    int negativeStyle;
    private View.OnClickListener negativeListener;

    public PuddingBuilder() {

    }

    public PuddingView create(final AppCompatActivity appCompatActivity) {
        PuddingView puddingView = new PuddingView(appCompatActivity);
        puddingView.setPuddingBuilder(this);
        String key = appCompatActivity.toString();
        if (puddingMap.containsKey(key)) {
            final PuddingView lastPudding = puddingMap.get(key);
            if (lastPudding != null && lastPudding.isAttachedToWindow()) {
                ViewCompat.animate(lastPudding).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        appCompatActivity.getWindowManager().removeViewImmediate(lastPudding);
                    }
                });
            }
        }
        puddingMap.put(key, puddingView);
        appCompatActivity.getLifecycle().addObserver(this);
        return puddingView;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onHostDestroy(LifecycleOwner lifecycleOwner) {
        String key = lifecycleOwner.toString();
        if (puddingMap.containsKey(key)) {
            PuddingView tmp = puddingMap.get(key);
            if (tmp != null) {
                tmp.hide(true);
            }
            puddingMap.remove(key);
        }
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    public PuddingBuilder setEnableIconAnimation() {
        enableIconAnimation = true;
        return this;
    }

    public PuddingBuilder setEnableInfiniteDuration() {
        enableInfiniteDuration = true;
        return this;
    }

    public PuddingBuilder setProgressMode() {
        enableProgress = true;
        return this;
    }

    public PuddingBuilder setEnableVibration() {
        enableVibration = true;
        return this;
    }

    public PuddingBuilder setEnableSwipeDismiss() {
        enableSwipeDismiss = true;
        return this;
    }

    public PuddingBuilder setPuddingCallBack(PuddingCallBack callBack) {
        puddingCallBack = callBack;
        return this;
    }

    public PuddingBuilder setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public PuddingBuilder setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public PuddingBuilder setSubTitleColor(@ColorInt int subTitleColor) {
        this.subTitleColor = subTitleColor;
        return this;
    }

    public PuddingBuilder setIconColor(@ColorInt int iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public PuddingBuilder setProgressColor(@ColorInt int progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public PuddingBuilder setBackgroundDrawableRes(@DrawableRes int backgroundDrawableRes) {
        this.backgroundDrawableRes = backgroundDrawableRes;
        return this;
    }

    public PuddingBuilder setIconDrawableRes(@DrawableRes int iconDrawableRes) {
        this.iconDrawableRes = iconDrawableRes;
        return this;
    }

    public PuddingBuilder setBackgroundDrawable(Drawable drawable) {
        this.backgroundDrawable = drawable;
        return this;
    }

    public PuddingBuilder setIconDrawable(Drawable drawable) {
        this.iconDrawable = drawable;
        return this;
    }

    public PuddingBuilder setIconFilter(ColorFilter colorFilter) {
        this.iconFilter = colorFilter;
        return this;
    }

    public PuddingBuilder setProgressFilter(ColorFilter colorFilter) {
        this.progressFilter = colorFilter;
        return this;
    }

    public PuddingBuilder setTitleText(String string) {
        this.titleText = string;
        return this;
    }

    public PuddingBuilder setSubTitleText(String string) {
        this.subTitleText = string;
        return this;
    }

    public PuddingBuilder setTitleTypeface(Typeface typeface) {
        this.titleTypeface = typeface;
        return this;
    }

    public PuddingBuilder setSubTitleTypeface(Typeface typeface) {
        this.subTitleTypeface = typeface;
        return this;
    }

    public PuddingBuilder setPositive(String showContext, @StyleRes int style, View.OnClickListener listener) {
        this.positiveText = showContext;
        this.positiveStyle = style;
        this.positiveListener = listener;
        return this;
    }

    public PuddingBuilder setNegative(String showContext, @StyleRes int style, View.OnClickListener listener) {
        this.negativeText = showContext;
        this.negativeStyle = style;
        this.negativeListener = listener;
        return this;
    }

    public PuddingCallBack getPuddingCallBack() {
        return puddingCallBack;
    }

    public boolean isEnableIconAnimation() {
        return enableIconAnimation;
    }

    public boolean isEnableInfiniteDuration() {
        return enableInfiniteDuration;
    }

    public boolean isEnableProgress() {
        return enableProgress;
    }

    public boolean isEnableVibration() {
        return enableVibration;
    }

    public boolean isEnableSwipeDismiss() {
        return enableSwipeDismiss;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getSubTitleColor() {
        return subTitleColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public int getIconColor() {
        return iconColor;
    }

    public int getBackgroundDrawableRes() {
        return backgroundDrawableRes;
    }

    public int getIconDrawableRes() {
        return iconDrawableRes;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public ColorFilter getIconFilter() {
        return iconFilter;
    }

    public ColorFilter getProgressFilter() {
        return progressFilter;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getSubTitleText() {
        return subTitleText;
    }

    public Typeface getTitleTypeface() {
        return titleTypeface;
    }

    public Typeface getSubTitleTypeface() {
        return subTitleTypeface;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public int getPositiveStyle() {
        return positiveStyle;
    }

    public View.OnClickListener getPositiveListener() {
        return positiveListener;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public int getNegativeStyle() {
        return negativeStyle;
    }

    public View.OnClickListener getNegativeListener() {
        return negativeListener;
    }
}