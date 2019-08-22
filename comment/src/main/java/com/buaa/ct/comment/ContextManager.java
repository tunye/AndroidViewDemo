package com.buaa.ct.comment;

import android.content.Context;


public class ContextManager {
    public static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    public static void setInstance(Context context) {
        mContext = context;
    }

    public static void destory() {
        mContext = null;
    }
}
