package com.buaa.ct.myapplication.sample.videocache;


import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.videocache.core.CacheListener;
import com.buaa.ct.videocache.httpproxy.HttpProxyCacheServer;

import java.io.File;
import java.io.IOException;

public class VideoCacheTestActivity extends CoreBaseActivity implements CacheListener {
    private HttpProxyCacheServer proxy;
    private String url = "http://sc1.111ttt.cn/2017/1/05/09/298092038446.mp3";
    private OnCacheListener onCacheListener;
    private MediaPlayer mediaPlayer;
    private TextView startPlay, percent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_videocache_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        startPlay = findViewById(R.id.video_cache_click);
        percent = findViewById(R.id.video_cache_progress);
    }

    @Override
    public void setListener() {
        super.setListener();
        startPlay.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                CustomToast.getInstance().showToast("已经开始播放");
                HttpProxyCacheServer proxy = getProxy();
                proxy.registerCacheListener(VideoCacheTestActivity.this, url);
                String proxyUrl = proxy.getProxyUrl(url);
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(proxyUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        toolbarOper.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                HttpProxyCacheServer proxy = getProxy();
                proxy.clearCache(url);
                percent.setText("");
                CustomToast.getInstance().showToast("已清除");
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_video_cache);
        enableToolbarOper(R.string.test_video_cache_oper);
        mediaPlayer = new MediaPlayer();
        checkCachedState();
    }

    private void checkCachedState() {
        HttpProxyCacheServer proxy = getProxy();
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached) {
            setCachedPercent(100);
            if (onCacheListener != null) {
                onCacheListener.getCacheProgress(100);
            }
        }
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        getProxy().shutdown();
        super.onDestroy();
    }

    private void setCachedPercent(int percentCount) {
        percent.setText("缓冲进度为：" + percentCount + "%");
    }

    public HttpProxyCacheServer getProxy() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        setCachedPercent(percentsAvailable);
        if (onCacheListener != null) {
            onCacheListener.getCacheProgress(percentsAvailable);
        }
    }

    public interface OnCacheListener {
        void getCacheProgress(int progress);
    }
}

