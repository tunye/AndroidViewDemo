package com.buaa.ct.easyui.recycler.layoutmanager.entity;

public class ItemViewInfo {
    private float mScaleXY;
    private float mLayoutPercent;
    private float mPositionOffset;
    private int mTop;
    private boolean mIsBottom;

    public ItemViewInfo(int top, float scaleXY, float positionOffset, float percent) {
        this(top, scaleXY, positionOffset, percent, false);
    }

    public ItemViewInfo(int top, float scaleXY, float positionOffset, float percent, boolean mIsBottom) {
        this.mTop = top;
        this.mScaleXY = scaleXY;
        this.mPositionOffset = positionOffset;
        this.mLayoutPercent = percent;
        this.mIsBottom = mIsBottom;
    }

    public float getScaleXY() {
        return mScaleXY;
    }

    public void setScaleXY(float mScaleXY) {
        this.mScaleXY = mScaleXY;
    }

    public float getLayoutPercent() {
        return mLayoutPercent;
    }

    public void setLayoutPercent(float mLayoutPercent) {
        this.mLayoutPercent = mLayoutPercent;
    }

    public float getPositionOffset() {
        return mPositionOffset;
    }

    public void setPositionOffset(float mPositionOffset) {
        this.mPositionOffset = mPositionOffset;
    }

    public int getTop() {
        return mTop;
    }

    public void setTop(int mTop) {
        this.mTop = mTop;
    }

    public boolean ismIsBottom() {
        return mIsBottom;
    }

    public void setmIsBottom(boolean mIsBottom) {
        this.mIsBottom = mIsBottom;
    }
}