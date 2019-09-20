package com.buaa.ct.myapplication.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class BingPic {
    @JSONField(name = "startdate")
    public String date;
    @JSONField(name = "enddate")
    public String dateEnd;
    @JSONField(name = "url")
    public String url;
    @JSONField(name = "urlbase")
    public String urlBase;
    @JSONField(name = "copyright")
    public String copyRight;
    @JSONField(name = "copyrightlink")
    public String copyRightUrl;
}
