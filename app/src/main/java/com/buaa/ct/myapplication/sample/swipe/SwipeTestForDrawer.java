package com.buaa.ct.myapplication.sample.swipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.SmartSwipeWrapper;
import com.buaa.ct.swipe.SwipeConsumer;
import com.buaa.ct.swipe.consumer.DrawerConsumer;
import com.buaa.ct.swipe.consumer.SlidingConsumer;
import com.buaa.ct.swipe.consumer.StretchConsumer;
import com.buaa.ct.swipe.listener.SimpleSwipeListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SwipeTestForDrawer extends BaseActivity {
    private NumberFormat formatter = new DecimalFormat("0.00");
    SmartSwipeWrapper horizontalMenuWrapper;

    SwipeConsumer mCurrentDrawerConsumer;
    DrawerConsumer mDrawerConsumer;
    SlidingConsumer mSlidingConsumer;
    boolean drawerOpened = false;

    private RadioGroup mode;
    private TextView factor;
    private SeekBar factorSeekBar;
    private TextView edgeSize;
    private SeekBar edgeSizeSeekBar;

    @Override
    public int getLayoutId() {
        return R.layout.swipe_test_drawer_activity;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        factor = findViewById(R.id.swipe_test_drawer_factor);
        factorSeekBar = findViewById(R.id.swipe_test_drawer_factor_seekbar);
        edgeSize = findViewById(R.id.swipe_test_drawer_egde);
        edgeSizeSeekBar = findViewById(R.id.swipe_test_drawer_egde_seekbar);
        mode = findViewById(R.id.swipe_test_drawer_mode);

        int size = RuntimeManager.getInstance().dip2px(300);
        View horizontalMenu = LayoutInflater.from(this).inflate(R.layout.swipe_test_drawer, null);
        horizontalMenu.setLayoutParams(new ViewGroup.LayoutParams(size, ViewGroup.LayoutParams.MATCH_PARENT));
        horizontalMenuWrapper = SmartSwipe.wrap(horizontalMenu).addConsumer(new StretchConsumer()).enableVertical().getWrapper();
    }

    @Override
    public void setListener() {
        back.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                if (drawerOpened) {
                    mCurrentDrawerConsumer.smoothClose();
                } else {
                    mCurrentDrawerConsumer.smoothLeftOpen();
                }
            }
        });
        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.swipe_test_drawer_mode_drawer) {
                    toggleConsumer(mDrawerConsumer);
                } else {
                    toggleConsumer(mSlidingConsumer);
                }
            }
        });
        factorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSlidingConsumer.setRelativeMoveFactor(progress * 0.01F);
                float result = mSlidingConsumer.getRelativeMoveFactor();
                factor.setText(formatter.format(result));
            }
        });
        edgeSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentDrawerConsumer.setEdgeSize(progress);
                if (progress == 0) {
                    edgeSize.setText("全屏");
                } else {
                    edgeSize.setText(progress + "px");
                }
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_swipe_bezier);

        mDrawerConsumer = new DrawerConsumer()
                //horizontal menu
                .setLeftDrawerView(horizontalMenuWrapper)
                //set the translucent color of scrim (default is 0:transparent)
                .setScrimColor(0x7F000000)
                //set the shadow color follow the drawer while swiping (default is 0:transparent)
                .setShadowColor(0x80000000)
                .setShadowSize(RuntimeManager.getInstance().dip2px(10))
                .addListener(listener)
                //set edge size to swipe to 20dp (default is 0: whole range of the contentView bounds)
                .setEdgeSize(RuntimeManager.getInstance().dip2px(20))
                .as(DrawerConsumer.class);

        mSlidingConsumer = new SlidingConsumer()
                .setDrawerExpandable(true)
                //horizontal menu
                .setLeftDrawerView(horizontalMenuWrapper)
                .showScrimAndShadowOutsideContentView()
                //set the translucent color of scrim (default is 0:transparent)
                .setScrimColor(0x7F000000)
                .setShadowSize(RuntimeManager.getInstance().dip2px(10))
                .setShadowColor(0x80000000)
                .addListener(listener)
                //set edge size to swipe to 20dp (default is 0: whole range of the contentView bounds)
                .setEdgeSize(RuntimeManager.getInstance().dip2px(20))
                .as(SlidingConsumer.class);

        mCurrentDrawerConsumer = mSlidingConsumer;
        updateBackImg(drawerOpened);
        toggleConsumer(mCurrentDrawerConsumer);
    }

    @Override
    public void onBackPressed() {
        if (drawerOpened) {
            mCurrentDrawerConsumer.smoothClose();
        } else {
            super.onBackPressed();
        }
    }

    private void updateBackImg(boolean open) {
        drawerOpened = open;
        if (open) {
            backIcon.setBackgroundResource(R.drawable.back);
        } else {
            backIcon.setBackgroundResource(R.drawable.menu);
        }
    }

    private void toggleConsumer(SwipeConsumer swipeConsumer) {
        //add swipe consumer to this activity
        mCurrentDrawerConsumer = SmartSwipe.wrap(this)
                //remove current consumer
                .removeConsumer(mCurrentDrawerConsumer)
                //add new consumer to this activity wrapper
                .addConsumer(swipeConsumer);
        mCurrentDrawerConsumer = swipeConsumer;
        findViewById(R.id.swipe_test_drawer_factor_ly).setVisibility(mCurrentDrawerConsumer == mSlidingConsumer ? View.VISIBLE : View.GONE);
        edgeSizeSeekBar.setProgress(mCurrentDrawerConsumer.getEdgeSize());
        if (mCurrentDrawerConsumer == mSlidingConsumer) {
            float factorValue = mSlidingConsumer.getRelativeMoveFactor();
            factorSeekBar.setProgress((int) (factorValue * 100));
        }
    }

    SimpleSwipeListener listener = new SimpleSwipeListener() {
        @Override
        public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
            super.onSwipeOpened(wrapper, consumer, direction);
            updateBackImg(true);
        }

        @Override
        public void onSwipeClosed(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
            super.onSwipeClosed(wrapper, consumer, direction);
            updateBackImg(false);
        }
    };
}
