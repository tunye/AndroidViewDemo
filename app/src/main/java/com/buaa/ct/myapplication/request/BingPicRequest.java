package com.buaa.ct.myapplication.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.buaa.ct.core.okhttp.RequestBase;
import com.buaa.ct.myapplication.entity.BingPic;

import java.util.List;

public class BingPicRequest extends RequestBase<List<BingPic>> {
    private String url;

    public BingPicRequest(int dayFromToday, int count) {
        url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=" + dayFromToday + "&n=" + count;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<BingPic> parseJsonImpl(JSONObject jsonObject) {
        return JSON.parseArray(jsonObject.getString("images"), BingPic.class);
    }
}
