package com.buaa.ct.videocache.core;

import com.buaa.ct.videocache.exception.ProxyCacheException;
import com.buaa.ct.videocache.httpproxy.HttpProxyCacheServer;
import com.buaa.ct.videocache.httpproxy.HttpUrlSource;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.buaa.ct.videocache.core.Preconditions.checkArgument;
import static com.buaa.ct.videocache.core.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Pings {@link HttpProxyCacheServer} to make sure it works.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */

public class Pinger {

    private static final String PING_REQUEST = "ping";
    private static final String PING_RESPONSE = "ping ok";

    private final ExecutorService pingExecutor = Executors.newSingleThreadExecutor();
    private final String host;
    private final int port;

    public Pinger(String host, int port) {
        this.host = checkNotNull(host);
        this.port = port;
    }

    public boolean ping(int maxAttempts, int startTimeout) {
        checkArgument(maxAttempts >= 1);
        checkArgument(startTimeout > 0);

        int timeout = startTimeout;
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                Future<Boolean> pingFuture = pingExecutor.submit(new PingCallable());
                boolean pinged = pingFuture.get(timeout, MILLISECONDS);
                if (pinged) {
                    return true;
                }
            } catch (TimeoutException e) {
            } catch (InterruptedException | ExecutionException e) {
            }
            attempts++;
            timeout *= 2;
        }
        String error = String.format("Error pinging server (attempts: %d, max timeout: %d). " +
                "If you see this message, please, email me danikula@gmail.com " +
                "or create issue here https://github.com/danikula/AndroidVideoCache/issues", attempts, timeout / 2);
        return false;
    }

    public boolean isPingRequest(String request) {
        return PING_REQUEST.equals(request);
    }

    public void responseToPing(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write("HTTP/1.1 200 OK\n\n".getBytes());
        out.write(PING_RESPONSE.getBytes());
    }

    private boolean pingServer() throws ProxyCacheException {
        String pingUrl = getPingUrl();
        HttpUrlSource source = new HttpUrlSource(pingUrl);
        try {
            byte[] expectedResponse = PING_RESPONSE.getBytes();
            source.open(0);
            byte[] response = new byte[expectedResponse.length];
            source.read(response);
            boolean pingOk = Arrays.equals(expectedResponse, response);
            return pingOk;
        } catch (ProxyCacheException e) {
            return false;
        } finally {
            source.close();
        }
    }

    private String getPingUrl() {
        return String.format(Locale.US, "http://%s:%d/%s", host, port, PING_REQUEST);
    }

    private class PingCallable implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            return pingServer();
        }
    }

}
