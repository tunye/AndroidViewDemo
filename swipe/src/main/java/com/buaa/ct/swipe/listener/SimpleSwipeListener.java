package com.buaa.ct.swipe.listener;

import com.buaa.ct.swipe.SmartSwipeWrapper;
import com.buaa.ct.swipe.SwipeConsumer;

/**
 * @author billy.qi
 */
public class SimpleSwipeListener implements SwipeListener {
    @Override
    public void onConsumerAttachedToWrapper(SmartSwipeWrapper wrapper, SwipeConsumer consumer) {

    }

    @Override
    public void onConsumerDetachedFromWrapper(SmartSwipeWrapper wrapper, SwipeConsumer consumer) {

    }

    @Override
    public void onSwipeStateChanged(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int state, int direction, float progress) {

    }

    @Override
    public void onSwipeStart(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {

    }

    @Override
    public void onSwipeProcess(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction, boolean settling, float progress) {

    }

    @Override
    public void onSwipeRelease(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction, float progress, float xVelocity, float yVelocity) {

    }

    @Override
    public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {

    }

    @Override
    public void onSwipeClosed(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {

    }
}
