package com.buaa.ct.easyui.pulldown;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.easyui.R;

public class FlexibleTestActivity extends CoreBaseActivity {
    public static final String URL_EXTRA = "url_extra";
    FlexibleView flexibleView;
    WebView web;
    TextView source;
    String url = "https://www.baidu.com";

    private ValueAnimator progressBarAnimator;
    private int mCurrentProgress;
    private View progressLayout;
    private ProgressBar loadProgress;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, FlexibleTestActivity.class);
        intent.putExtra(URL_EXTRA, url);
        context.startActivity(intent);
    }

    @Override
    public void beforeSetLayout(Bundle savedInstanceState) {
        super.beforeSetLayout(savedInstanceState);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(URL_EXTRA)) {
            url = getIntent().getExtras().getString(URL_EXTRA);
        }
    }

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
        loadProgress = findViewById(R.id.load_progress);
        progressLayout = findViewById(R.id.load_progress_layout);
        progressBarAnimator = new ValueAnimator().setDuration(300);
        progressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                loadProgress.getLayoutParams().width = (int) (progress / 100f * RuntimeManager.getInstance().getScreenWidth());
                loadProgress.requestLayout();
            }
        });
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
        web.setWebChromeClient(
                new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        startProgressBarAnim(newProgress);
                        super.onProgressChanged(view, newProgress);
                    }

                    @Override
                    public void onReceivedTitle(WebView view, String titleContent) {
                        super.onReceivedTitle(view, titleContent);
                        if (TextUtils.isEmpty(titleContent)) {
                            title.setText(R.string.web_flexible_test);
                        } else {
                            title.setText(titleContent.length() > 15 ? titleContent.substring(0, 14) + "..." : titleContent);
                        }
                    }
                }
        );
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // [bug:86726] android 5.0增强安全机制，不允许https http混合，加此配置解决
            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        web.loadUrl(url);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
        source.setText("本网页由 " + Uri.parse(url).getHost() + " 提供");

        back.findViewById(R.id.back_img).setBackgroundResource(R.drawable.close);
        loadProgress.setMax(100);
        loadProgress.setProgress(0);
    }

    private void startProgressBarAnim(int newProgress) {
        if (mCurrentProgress == newProgress) {
            return;
        }
        if (progressBarAnimator.isRunning()) {
            progressBarAnimator.end();
        }
        progressBarAnimator.setIntValues(mCurrentProgress, newProgress);
        if (newProgress >= 100) {
            progressLayout.setVisibility(View.GONE);
        } else {
            progressLayout.setVisibility(View.VISIBLE);
        }
        progressBarAnimator.start();
        mCurrentProgress = newProgress;
    }
}
