package com.buaa.ct.core.okhttp;

public interface IRequestCallBack<T> {
    void onStart();

    void onSuccess(T t);

    void onFinish();

    void onError(ErrorInfoWrapper errorInfo);
}
