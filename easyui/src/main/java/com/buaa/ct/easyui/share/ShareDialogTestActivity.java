package com.buaa.ct.easyui.share;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.util.ThreadUtils;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.share.listener.SimpleAnimationListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class ShareDialogTestActivity extends CoreBaseActivity {
    FloatingActionButton fab;
    View share;
    View cancel;

    View qq, wb, wx, yx;

    boolean shown;

    @Override
    public int getLayoutId() {
        return R.layout.share_dialog_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        share = findViewById(R.id.share);
        cancel = findViewById(R.id.cancel);
        share.setAlpha(0);
        fab = findViewById(R.id.fab);

        qq = findViewById(R.id.qq);
        wx = findViewById(R.id.wx);
        wb = findViewById(R.id.wb);
        yx = findViewById(R.id.yx);
    }

    @Override
    public void setListener() {
        super.setListener();
        fab.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                if (!shown) {
                    shown = true;
                    YoYo.with(Techniques.SlideInUp).interpolate(new AccelerateDecelerateInterpolator()).duration(200).withListener(new SimpleAnimationListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            qq.setAlpha(0);
                            wx.setAlpha(0);
                            wb.setAlpha(0);
                            yx.setAlpha(0);
                            final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
                            ThreadUtils.postOnUiThreadDelay(new Runnable() {
                                @Override
                                public void run() {
                                    YoYo.with(Techniques.BounceIn).interpolate(interpolator).duration(400).playOn(qq);
                                }
                            }, 50);
                            ThreadUtils.postOnUiThreadDelay(new Runnable() {
                                @Override
                                public void run() {
                                    YoYo.with(Techniques.BounceIn).interpolate(interpolator).duration(400).playOn(wx);
                                }
                            }, 100);
                            ThreadUtils.postOnUiThreadDelay(new Runnable() {
                                @Override
                                public void run() {
                                    YoYo.with(Techniques.BounceIn).interpolate(interpolator).duration(400).playOn(wb);
                                }
                            }, 150);
                            ThreadUtils.postOnUiThreadDelay(new Runnable() {
                                @Override
                                public void run() {
                                    YoYo.with(Techniques.BounceIn).interpolate(interpolator).duration(400).playOn(yx);
                                }
                            }, 200);
                        }
                    }).duration(250).playOn(share);
                }
            }
        });
        cancel.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                fadeoutShareDialog();
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.share_test);
    }

    @Override
    public void onBackPressed() {
        if (shown) {
            fadeoutShareDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void fadeoutShareDialog() {
        shown = false;
        YoYo.with(Techniques.FadeOut).interpolate(new AccelerateDecelerateInterpolator()).duration(200).playOn(share);
    }
}
