package com.buaa.ct.core.listener;

import android.view.MotionEvent;
import android.view.View;

import com.buaa.ct.core.view.CustomToast;

/**
 * Created by 10202 on 2015/12/21.
 */
public class IOnDoubleClick implements View.OnTouchListener {
    private int count;
    private long firClick, secClick;
    private String failMsg;
    private IOnClickListener onClickListener;
    private int DOUBLE_INTERVAL = 800;

    public IOnDoubleClick(IOnClickListener onClickListener, String msg) {
        this.onClickListener = onClickListener;
        this.failMsg = msg;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            count++;
            if (count == 1) {
                firClick = System.currentTimeMillis();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 1) {
                            count = 0;
                            firClick = 0;
                            secClick = 0;
                            CustomToast.getInstance().showToast(failMsg);
                        }
                    }
                }, DOUBLE_INTERVAL);
            } else if (count == 2) {
                secClick = System.currentTimeMillis();
                if (secClick - firClick < DOUBLE_INTERVAL) {
                    onClickListener.onClick(v, "click");
                }
                count = 0;
                firClick = 0;
                secClick = 0;
            }
        }
        return true;
    }
}
