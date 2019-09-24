package com.buaa.ct.myapplication.sample.swipe;

import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.consumer.ActivitySlidingBackConsumer;

public class SwipeTestForSlide extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.swipe_test_activity;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        SmartSwipe.wrap(this)
                .removeAllConsumers()
                .addConsumer(new ActivitySlidingBackConsumer(this))
                .setRelativeMoveFactor(0.5f)
                .enableAllDirections()
                .as(ActivitySlidingBackConsumer.class);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_swipe_wechat_slide);
    }
}
