package com.buaa.ct.easyui.share;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.share.manager.RuntimeManager;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class ShareDialogTestActivity extends AppCompatActivity {
    FloatingActionButton fab;
    View share;
    View cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog_test);
        RuntimeManager.setApplication(getApplication());
        RuntimeManager.setApplicationContext(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        share = findViewById(R.id.share);
        cancel = findViewById(R.id.cancel);
        share.setVisibility(View.GONE);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (share.getVisibility() != View.VISIBLE) {
                    YoYo.with(Techniques.SlideInUp).interpolate(new AccelerateDecelerateInterpolator()).duration(200).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            share.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.BounceIn).interpolate(new AccelerateDecelerateInterpolator()).delay(50).duration(400).playOn(findViewById(R.id.qq));
                            YoYo.with(Techniques.BounceIn).interpolate(new AccelerateDecelerateInterpolator()).delay(100).duration(400).playOn(findViewById(R.id.wx));
                            YoYo.with(Techniques.BounceIn).interpolate(new AccelerateDecelerateInterpolator()).delay(150).duration(400).playOn(findViewById(R.id.wb));
                            YoYo.with(Techniques.BounceIn).interpolate(new AccelerateDecelerateInterpolator()).delay(200).duration(400).playOn(findViewById(R.id.yx));
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).duration(250).playOn(share);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeoutShareDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (share.getVisibility() == View.VISIBLE) {
            fadeoutShareDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void fadeoutShareDialog() {
        YoYo.with(Techniques.FadeOut).interpolate(new AccelerateDecelerateInterpolator()).duration(200).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                share.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(share);
    }
}
