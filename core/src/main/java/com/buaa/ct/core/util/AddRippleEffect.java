package com.buaa.ct.core.util;

import android.view.View;

import com.buaa.ct.core.R;
import com.buaa.ct.core.view.MaterialRippleLayout;

public class AddRippleEffect {
    public static void addRippleEffect(View view) {
        addRippleEffect(view, 150);
    }

    public static void addRippleEffect(View view, int duration) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleAlpha(0.2f)
                .rippleDuration(duration)
                .rippleColor(view.getContext().getResources().getColor(R.color.text_complementary))
                .rippleHover(true)
                .create();
    }
}