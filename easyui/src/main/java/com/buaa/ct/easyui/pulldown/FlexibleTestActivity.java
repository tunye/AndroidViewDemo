package com.buaa.ct.easyui.pulldown;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;

public class FlexibleTestActivity extends CoreBaseActivity {
    FlexibleView flexibleView;
    WebView web;
    TextView source;

    @Override
    public int getLayoutId() {
        return R.layout.flexible_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        flexibleView = findViewById(R.id.flexible);
        web = findViewById(R.id.webview);
        source = findViewById(R.id.source);
    }

    @Override
    public void setListener() {
        super.setListener();
        web.setDownloadListener(new DownloadListener() {
            @Override

            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        web.loadUrl("https://www.baidu.com");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
        source.setText("本网页由 " + "www.baidu.com" + " 提供");

        title.setText(R.string.web_flexible_test);
        back.findViewById(R.id.back_img).setBackgroundResource(R.drawable.close);
    }
}
