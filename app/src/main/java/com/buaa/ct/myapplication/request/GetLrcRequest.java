package com.buaa.ct.myapplication.request;

import com.alibaba.fastjson.JSONObject;
import com.buaa.ct.core.okhttp.RequestBase;

import okhttp3.Headers;

public class GetLrcRequest extends RequestBase<String[]> {
    private String url;

    public GetLrcRequest() {
        url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.lry&format=json&callback=&songid=2107024&_=" + System.currentTimeMillis();
    }

    @Override
    public Headers getHeader() {
        // 百度直接用okhttp agent会出现403
        Headers.Builder builder = new Headers.Builder();
        builder.add("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        builder.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        builder.add("accept-encoding", "gzip");
        return builder.build();
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String[] parseJsonImpl(JSONObject jsonObject) {
        return jsonObject.getString("lrcContent").split("\\n");
    }

    //百度查询歌曲列表
    //
    //http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.search.catalogSug&format=json&callback=&query=%E5%91%A8%E6%9D%B0%E4%BC%A6&_=1413017198449
    //
    //百度查询歌曲信息
    //
    //http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.getInfo&format=json&callback=&songid=2113203&_=1413017198449
    //
    //百度获取歌词
    //
    //http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.lry&format=json&callback=&songid=2107024&_=1413017198449
    //
    //参考资料
    //
    //https://mrasong.com/a/baidu-mp3-api-full/comment-p-4#comments
}
