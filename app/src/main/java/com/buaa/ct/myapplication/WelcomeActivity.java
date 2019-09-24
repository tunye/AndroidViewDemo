package com.buaa.ct.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.okhttp.ErrorInfoWrapper;
import com.buaa.ct.core.okhttp.RequestClient;
import com.buaa.ct.core.okhttp.SimpleRequestCallBack;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.util.ImageUtil;
import com.buaa.ct.core.util.MyPalette;
import com.buaa.ct.core.util.PermissionPool;
import com.buaa.ct.core.view.progressbar.RoundProgressBar;
import com.buaa.ct.myapplication.entity.BingPic;
import com.buaa.ct.myapplication.request.BingPicRequest;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

import java.util.List;

public class WelcomeActivity extends BaseActivity {
    public static final int HANDLER_AD_PROGRESS = 0;
    private View escapeAd;
    private ImageView img;
    private TextView picCopyRight;
    private RoundProgressBar welcomeAdProgressbar;              // 等待进度条
    private BingPic bingPic;                                    // 开屏图片
    private MyPalette palette;

    @Override
    public void beforeSetLayout(Bundle saveBundle) {
        super.beforeSetLayout(saveBundle);
        palette = new MyPalette();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getBannerPic();
    }

    @Override
    public int getLayoutId() {
        return R.layout.welcome;
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        requestMultiPermission(new int[]{PermissionPool.CAMERA, PermissionPool.WRITE_EXTERNAL_STORAGE}, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
    }

    @Override
    public void onAccreditSucceed(int requestCode) {
        super.onAccreditSucceed(requestCode);
        initWelcomeAdProgress();
    }

    @Override
    public void onAccreditFailure(int requestCode) {
        super.onAccreditFailure(requestCode);
        finish();
    }

    @Override
    public void initWidget() {
        welcomeAdProgressbar = findViewById(R.id.welcome_ad_progressbar);
        escapeAd = findViewById(R.id.welcome_escape_ad);
        img = findViewById(R.id.welcome_img);
        picCopyRight = findViewById(R.id.welcome_pic_copyright);
    }

    @Override
    public void setListener() {
        escapeAd.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View view) {
                handler.removeMessages(HANDLER_AD_PROGRESS);
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initWelcomeAdProgress() {
        welcomeAdProgressbar.setCricleProgressColor(GetAppColor.getInstance().getAppColor());
        welcomeAdProgressbar.setProgress(150);                  // 为progress设置一个初始值
        welcomeAdProgressbar.setMax(4000);                      // 总计等待4s
        handler.sendEmptyMessageDelayed(HANDLER_AD_PROGRESS, 500);                // 半秒刷新进度
    }

    private void getBannerPic() {
        if (isNetworkAvailable()) {
            RequestClient.requestAsync(new BingPicRequest(0, 1), new SimpleRequestCallBack<List<BingPic>>() {
                @Override
                public void onSuccess(final List<BingPic> bingPics) {
                    bingPic = bingPics.get(0);
                    if (bingPic.copyRight.contains("(")) {
                        String[] section = bingPic.copyRight.split("\\(");
                        bingPic.copyRight = section[0] + "\n(" + section[1];
                    }
                    picCopyRight.setText(bingPic.copyRight);
                    ImageUtil.loadImage("http://s.cn.bing.net" + bingPic.urlBase+"_768x1280.jpg&rf=LaDigue_768x1280.jpg", img, null, new ImageUtil.OnBitmapLoaded() {
                        @Override
                        public void onImageLoaded(Bitmap bitmap) {
                            palette.getByBitmap(bitmap, new MyPalette.PaletteFinish() {
                                @Override
                                public void finish() {
                                    if (palette.getPalette().getLightVibrantSwatch() != null) {
                                        picCopyRight.setTextColor(palette.getPalette().getLightVibrantSwatch().getBodyTextColor());
                                    } else if (palette.getPalette().getLightMutedSwatch() != null) {
                                        picCopyRight.setTextColor(palette.getPalette().getLightMutedSwatch().getBodyTextColor());
                                    } else {
                                        for (Palette.Swatch swatch : palette.getPalette().getSwatches()) {
                                            if (swatch != null) {
                                                picCopyRight.setTextColor(swatch.getBodyTextColor());
                                                break;
                                            }
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void onImageLoadFailed() {

                        }
                    });
                }

                @Override
                public void onError(ErrorInfoWrapper errorInfoWrapper) {
                    setDefaultImg();
                }
            });
        } else {
            setDefaultImg();
        }
    }

    private void setDefaultImg() {
        img.setImageResource(R.drawable.help1);
        palette.getByResource(R.drawable.help1, new MyPalette.PaletteFinish() {
            @Override
            public void finish() {
                picCopyRight.setTextColor(palette.getLightVibrantColor());
            }
        });
    }

    private boolean isNetworkAvailable() {
        Context context = RuntimeManager.getInstance().getApplication();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
            if (activeInfo != null) {
                return activeInfo.isConnected();
            } else {
                return false;
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int progress = welcomeAdProgressbar.getProgress();
            if (progress < 4000) {
                progress = progress < 500 ? 500 : progress + 500;
                welcomeAdProgressbar.setProgress(progress);
                handler.sendEmptyMessageDelayed(HANDLER_AD_PROGRESS, 500);
            } else {
                welcomeAdProgressbar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            return false;
        }
    });
}
