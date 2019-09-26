package com.buaa.ct.swipe.support;

import android.content.Context;

import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.SmartSwipeWrapper;

/**
 * @author billy.qi
 */
public class WrapperFactory implements SmartSwipe.IWrapperFactory {
    @Override
    public SmartSwipeWrapper createWrapper(Context context) {
        return new SmartSwipeWrapperSupport(context);
    }
}
