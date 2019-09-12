package com.buaa.ct.core.okhttp;

import android.text.TextUtils;

public class ErrorInfoWrapper {
    public static final int DATA_ERROR = 1;
    public static final int NET_ERROR = 2;
    public static final int EMPTY_ERROR = 3;
    public int type;
    public String errorMsg;

    public ErrorInfoWrapper(int type) {
        this.type = type;
    }

    public ErrorInfoWrapper(int type, String errorMsg) {
        this.type = type;
        this.errorMsg = errorMsg;
    }

    public static ErrorInfoWrapper parseOkhttpError(String errorMsg) {
        if (TextUtils.isEmpty(errorMsg)) {
            return new ErrorInfoWrapper(DATA_ERROR);
        }
        switch (errorMsg) {
            case WebRequest.NET_ERR:
                return new ErrorInfoWrapper(NET_ERROR);
            case WebRequest.EMPTY_BODY:
                return new ErrorInfoWrapper(EMPTY_ERROR);
            default:
            case WebRequest.SERVER_ERR:
                return new ErrorInfoWrapper(DATA_ERROR);
        }
    }
}
