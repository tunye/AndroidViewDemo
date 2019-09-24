package com.buaa.ct.myapplication.sample.swipe;

import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.SwipeConsumer;
import com.buaa.ct.swipe.consumer.ActivityShuttersBackConsumer;

public class SwipeTestForShutters extends BaseActivity {
    private SwipeConsumer swipeConsumer;

    @Override
    public int getLayoutId() {
        return R.layout.swipe_test_activity;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        swipeConsumer = SmartSwipe.wrap(this)
                .removeAllConsumers()
                .addConsumer(new ActivityShuttersBackConsumer(this))
                .setScrimColor(0x7f000000)
                .enableAllDirections()
                .as(ActivityShuttersBackConsumer.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (swipeConsumer != null) {
            swipeConsumer.smoothLeftOpen();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_swipe_shutters);
    }
}
