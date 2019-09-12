package com.buaa.ct.core.util;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class SpringUtil {
    private Spring mSpring;

    private SpringUtil() {
        mSpring = SpringSystem.create().createSpring();
        mSpring.setSpringConfig(new SpringConfig(75, 6));
        mSpring.setOvershootClampingEnabled(true);
    }

    public static SpringUtil getInstance() {
        return InstanceHelper.instance;
    }

    public Spring getmSpring() {
        return mSpring;
    }

    public void addListener(SimpleSpringListener listener) {
        mSpring.addListener(listener);
    }

    public void destory() {
        mSpring.removeAllListeners();
    }

    public void setEndValue(double value) {
        mSpring.setEndValue(value);
    }

    public double getCurrentValue() {
        return mSpring.getCurrentValue();
    }

    public void setCurrentValue(double value) {
        mSpring.setCurrentValue(value);
    }

    private static class InstanceHelper {
        private static SpringUtil instance = new SpringUtil();
    }
}
