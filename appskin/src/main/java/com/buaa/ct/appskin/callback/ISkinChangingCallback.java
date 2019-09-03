package com.buaa.ct.appskin.callback;

/**
 * Created by ct on 19/9/2.
 */
public interface ISkinChangingCallback {

    void onStart();

    void onError(Exception e);

    void onComplete();

}
