package com.buaa.ct.core.network;

import com.buaa.ct.core.util.ThreadPoolUtil;

import java.io.IOException;

/**
 * Created by 10202 on 2015/10/13.
 */
public class PingIPThread {
    private static final String ip = "223.5.5.5";                          //阿里ip
    private PingResult pingResult;

    public PingIPThread(PingResult pingResult) {
        this.pingResult = pingResult;
    }

    public void start() {
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                Process ipProcess = null;
                try {
                    //-c 后边跟随的是重复的次数，-w后边跟随的是超时的时间，单位是秒
                    ipProcess = runtime.exec("ping -c 2 -w 2 " + ip);
                    int exitValue = ipProcess.waitFor();
                    if (exitValue == 0) {
                        pingResult.success();
                    } else {
                        pingResult.fail();
                    }
                } catch (IOException | InterruptedException e) {
                    pingResult.fail();
                } finally {
                    //在结束的时候应该对资源进行回收
                    if (ipProcess != null) {
                        ipProcess.destroy();
                    }
                    runtime.gc();
                }
            }
        });
    }

    public interface PingResult {
        void success();

        void fail();
    }
}
