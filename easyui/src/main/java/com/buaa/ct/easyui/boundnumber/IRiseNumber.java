package com.buaa.ct.easyui.boundnumber;

/**
 * Created by ct on 2015/8/19.
 */
public interface IRiseNumber {
    public void start();

    public RiseNumberTextView withNumber(float number);

    public RiseNumberTextView withNumber(float number, boolean flag);

    public RiseNumberTextView withNumber(int number, boolean flag);

    public RiseNumberTextView withNumber(int number);

    public RiseNumberTextView setDuration(long duration);

    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
