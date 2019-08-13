package com.buaa.ct.videocache.core;

import com.buaa.ct.videocache.exception.ProxyCacheException;

/**
 * Cache for proxy.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface Cache {

    int available() throws ProxyCacheException;

    int read(byte[] buffer, long offset, int length) throws ProxyCacheException;

    void append(byte[] data, int length) throws ProxyCacheException;

    void close() throws ProxyCacheException;

    void complete() throws ProxyCacheException;

    boolean isCompleted();
}
