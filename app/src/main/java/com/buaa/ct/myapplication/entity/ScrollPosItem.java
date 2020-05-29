package com.buaa.ct.myapplication.entity;

public class ScrollPosItem {
    public String showText;
    public int index;
    public int initPos;

    public ScrollPosItem(int i) {
        showText = "Pos " + i;
        index = i;
        initPos = i;
    }
}
