package com.buaa.ct.core.okhttp;

import android.util.Pair;

import com.buaa.ct.core.network.NetWorkState;
import com.buaa.ct.core.util.ThreadPoolUtil;
import com.buaa.ct.core.util.ThreadUtils;

public class RequestClient {
    public static <TResult> void requestAsync(final RequestBase<TResult> request,
                                              final IRequestCallBack<TResult> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }
        boolean noNet = !NetWorkState.getInstance().isConnectByCondition(NetWorkState.ALL_NET);
        if (noNet) {
            callback.onError(ErrorInfoWrapper.parseOkhttpError(WebRequest.NET_ERR));
            callback.onFinish();
            return;
        }
        callback.onStart();
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                final Pair<String, String> result;
                switch (request.getRequestType()) {
                    default:
                    case RequestBase.REQUEST_TYPE_GET:
                        result = WebRequest.getInstance().getRequest(request.getUrl(), request.getHeader());
                        break;
                    case RequestBase.REQUEST_TYPE_POST:
                        result = WebRequest.getInstance().postRequest(request.getUrl(), request.getRequestBody(), request.getHeader());
                        break;
                    case RequestBase.REQUEST_TYPE_PUT:
                        WebRequest.getInstance().putRequest(request.getUrl(), request.getRequestBody(), request.getHeader());
                        result = new Pair<>(WebRequest.SUCCESS, "");
                        break;
                }
                if (result.first.equals(WebRequest.SUCCESS) && result.second != null) {
                    ThreadUtils.postOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final TResult parseResult = request.parse(result.second);
                                if (parseResult != null) {
                                    callback.onSuccess(parseResult);
                                } else {
                                    callback.onError(new ErrorInfoWrapper(ErrorInfoWrapper.EMPTY_ERROR, request.getDataErrorMsg()));
                                }
                            } catch (Exception e) {
                                callback.onError(new ErrorInfoWrapper(ErrorInfoWrapper.DATA_ERROR, ""));
                            }
                        }
                    });
                } else {
                    ThreadUtils.postOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(ErrorInfoWrapper.parseOkhttpError(result.second));
                        }
                    });
                }
            }
        });
    }
}
