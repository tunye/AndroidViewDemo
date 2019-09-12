package com.buaa.ct.core.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import okhttp3.Headers;
import okhttp3.RequestBody;

public class RequestBase<T> {
    public static final int REQUEST_TYPE_GET = 0;
    public static final int REQUEST_TYPE_POST = 1;
    public static final int REQUEST_TYPE_PUT = 2;

    public static final int JSON_DATA = 0;
    public static final int XML_DATA = 1;
    public static final int STRING_DATA = 2;

    public int returnDataType;

    public int getRequestType() {
        return REQUEST_TYPE_GET;
    }

    public String getUrl() {
        return "";
    }

    public Headers getHeader() {
        return null;
    }

    public RequestBody getRequestBody() {
        return null;
    }

    public final T parse(String webResponse) {
        if (returnDataType == JSON_DATA) {
            return parseJson(webResponse);
        } else if (returnDataType == XML_DATA) {
            return parseXml(webResponse);
        } else {
            return parseStringImpl(webResponse);
        }
    }

    private T parseJson(String webResponse) {
        return parseJsonImpl(JSON.parseObject(webResponse));
    }

    public T parseJsonImpl(JSONObject jsonObject) {
        return null;
    }

    private T parseXml(String webResponse) {
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(webResponse));
            return parseXmlImpl(xmlPullParser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T parseXmlImpl(XmlPullParser xmlPullParser) {
        return null;
    }

    public T parseStringImpl(String response) {
        return null;
    }
}
