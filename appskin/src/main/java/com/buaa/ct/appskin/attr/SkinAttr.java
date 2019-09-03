package com.buaa.ct.appskin.attr;

import android.view.View;

/**
 * Created by ct on 19/9/2.
 */

public class SkinAttr {
    String resName;
    SkinAttrType attrType;


    public SkinAttr(SkinAttrType attrType, String resName) {
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view) {
        attrType.apply(view, resName);
    }
}
