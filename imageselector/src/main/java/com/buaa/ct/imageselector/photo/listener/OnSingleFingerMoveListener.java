package com.buaa.ct.imageselector.photo.listener;

public interface OnSingleFingerMoveListener {

    void move(float diffX, float diffY);
    void release(float diffX, float diffY,boolean cancel);
}