package com.buaa.ct.myapplication.sample.swipe;

import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.SmartSwipeWrapper;
import com.buaa.ct.swipe.SwipeConsumer;
import com.buaa.ct.swipe.consumer.ActivityDoorBackConsumer;
import com.buaa.ct.swipe.consumer.DoorConsumer;
import com.buaa.ct.swipe.consumer.StayConsumer;
import com.buaa.ct.swipe.listener.SimpleSwipeListener;

public class SwipeTestForStay extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.swipe_test_activity;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        SmartSwipe.wrap(this)
                .removeAllConsumers()
                .addConsumer(new StayConsumer())
                .enableAllDirections()
                .as(StayConsumer.class)
                .addListener(new SimpleSwipeListener() {
                    @Override
                    public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        finish();
                    }
                });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_swipe_qq_stay);
    }
}
