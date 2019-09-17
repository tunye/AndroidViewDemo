package com.buaa.ct.appskin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buaa.ct.appskin.ResourceManager;
import com.buaa.ct.appskin.SkinManager;

/**
 * Created by ct on 19/9/2.
 */
public class SkinAttrType implements ISkinAttrType {
    public static final SkinAttrType BACKGROUD = new SkinAttrType("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable == null) return;
            view.setBackgroundDrawable(drawable);
        }
    };
    public static final SkinAttrType COLOR = new SkinAttrType("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList colorlist = getResourceManager().getColorStateList(resName);
            if (colorlist == null) return;
            ((TextView) view).setTextColor(colorlist);
        }
    };
    public static final SkinAttrType PROGRESS = new SkinAttrType("progressDrawable") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable == null) return;
            ((ProgressBar) view).setProgressDrawable(drawable);
        }
    };
    public static final SkinAttrType SRC = new SkinAttrType("src") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ImageView) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable == null) return;
                ((ImageView) view).setImageDrawable(drawable);
            }
        }
    };
    private static SkinAttrType[] types = new SkinAttrType[]{BACKGROUD, COLOR, PROGRESS, SRC};
    private String attrType;

    SkinAttrType(String attrType) {
        this.attrType = attrType;
    }

    public static SkinAttrType[] values() {
        return types;
    }

    public String getAttrType() {
        return attrType;
    }

    ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourceManager();
    }

    @Override
    public void apply(View view, String resName) {

    }
}
