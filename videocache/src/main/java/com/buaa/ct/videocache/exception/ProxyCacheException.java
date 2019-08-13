package com.buaa.ct.videocache.exception;

import com.buaa.ct.videocache.httpproxy.ProxyCache;

/**
 * Indicates any error in work of {@link ProxyCache}.
 *
 * @author Alexey Danilov
 */
public class ProxyCacheException extends Exception {

    public ProxyCacheException(String message) {
        super(message);
    }

    public ProxyCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyCacheException(Throwable cause) {
        super(cause);
    }
}
