package com.buaa.ct.pudding;

import android.view.View;

public interface DismissCallBack {
    boolean canDismiss();

    void onDismiss(View view);

    void onTouch(View view, boolean touching);
}
