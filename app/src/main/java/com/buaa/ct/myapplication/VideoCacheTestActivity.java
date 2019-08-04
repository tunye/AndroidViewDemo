package com.buaa.ct.myapplication;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.buaa.ct.videocache.CacheListener;
import com.buaa.ct.videocache.HttpProxyCacheServer;

import java.io.File;
import java.io.IOException;

public class VideoCacheTestActivity extends AppCompatActivity implements CacheListener {
    private HttpProxyCacheServer proxy;
    private String url = "http://sc1.111ttt.cn/2017/1/05/09/298092038446.mp3";
    private OnCacheListener onCacheListener;
    private MediaPlayer mediaPlayer;
    private TextView startPlay, percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocache);
        startPlay = findViewById(R.id.video_cache_click);
        percent = findViewById(R.id.video_cache_progress);

        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

