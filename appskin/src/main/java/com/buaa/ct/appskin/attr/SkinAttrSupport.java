package com.buaa.ct.appskin.attr;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ct on 19/9/2.
 */

public class SkinAttrSupport {
    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        SkinAttr skinAttr = null;
        SkinAttrType attrType;
        String attrName, attrValue, entryName;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            attrName = attrs.getAttributeName(i);
            attrValue = attrs.getAttributeValue(i);

            attrType = getSupportAttrType(attrName);
            if (attrType == null) continue;

            if (attrValue.startsWith("@")) {
                int id = Integer.parseInt(attrValue.substring(1));
                if (id != 0) {
                    entryName = context.getResources().getResourceEntryName(id);
                    if (entryName.startsWith("skin")) {
                        skinAttr = new SkinAttr(attrType, entryName);
                        skinAttrs.add(skinAttr);
                    }
                }
            }
        }
        return skinAttrs;
    }

    private static SkinAttrType getSupportAttrType(String attrName) {
        for (SkinAttrType attrType : SkinAttrType.values()) {
            if (attrType.getAttrType().equals(attrName))
                return attrType;
        }
        return null;
    }
}
