package com.buaa.ct.core.okhttp;

import android.text.TextUtils;
import android.util.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by chentong1 on 2017/12/14.
 */
public class WebRequest {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String EMPTY_BODY = "EMPTY_BODY";
    public static final String SERVER_ERR = "SERVER_ERR";
    public static final String NET_ERR = "NET_ERR";
    private OkHttpClient okHttpClient;

    private WebRequest() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(5, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.retryOnConnectionFailure(true);
        okHttpClient = okHttpClientBuilder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static RequestBody getJSONRequestBody(String data) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON, data);
    }

    public static WebRequest getInstance() {
        return InstanceHelper.instance;
    }

    public Pair<String, String> getRequest(String url) {
        return getRequest(url, null);
    }

    public Pair<String, String> getRequest(String url, Headers headers) {
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (headers != null) {
            request.headers(headers);
        }
        return execute(request.build());
    }

    public Pair<String, String> postRequest(String url, RequestBody requestBody) {
        return postRequest(url, requestBody, null);
    }

    public Pair<String, String> postRequest(String url, RequestBody requestBody, Headers headers) {
        Request.Builder request = new Request.Builder();
        request.url(url);
        request.post(requestBody);
        if (headers != null) {
            request.headers(headers);
        }
        return execute(request.build());
    }

    public void putRequest(String url, RequestBody requestBody) {
        putRequest(url, requestBody, null);
    }

    public void putRequest(String url, RequestBody requestBody, Headers headers) {
        Request.Builder request = new Request.Builder();
        request.url(url);
        request.put(requestBody);
        if (headers != null) {
            request.headers(headers);
        }
        execute(request.build());
    }

    private Pair<String, String> execute(Request request) {
        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            if (response.code() > 300) {
                response.close();
                return new Pair<>(FAIL, SERVER_ERR);
            }
            String result = response.body() != null ? response.body().string() : null;
            response.close();
            if (TextUtils.isEmpty(result)) {
                return new Pair<>(FAIL, EMPTY_BODY);
            } else {
                return new Pair<>(SUCCESS, result);
            }
        } catch (IOException e) {
            if (response != null) {
                response.close();
            }
            return new Pair<>(FAIL, NET_ERR);
        }
    }

    private String getRequestParam(Request request) {
        Buffer buffer = new Buffer();
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (charset == null) {
                charset = Charset.forName("UTF-8");
            }
            return buffer.readString(charset);
        }
        return "";
    }

    private static class InstanceHelper {
        private static WebRequest instance = new WebRequest();
    }
}
