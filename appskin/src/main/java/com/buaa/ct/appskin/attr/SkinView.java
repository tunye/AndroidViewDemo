package com.buaa.ct.appskin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by ct on 19/9/2.
 */
public class SkinView {
    View view;
    List<SkinAttr> attrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.view = view;
        this.attrs = skinAttrs;
    }

    public void apply() {
        if (view == null) return;

        for (SkinAttr attr : attrs) {
            attr.apply(view);
        }
    }
}
