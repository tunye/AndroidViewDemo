package com.buaa.ct.easyui.progressimage;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;

import java.util.Random;

public class ProgressImageTestActivity extends CoreBaseActivity {
    private ProgressImageView progressImageView;
    private int progress;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    CustomToast.getInstance().showToast("启动模拟");
                    startFake();
                    break;
                case 1:
                    progress += new Random().nextInt(10);
                    progressImageView.setProgress(progress);
                    if (progress < 100) {
                        handler.sendEmptyMessageDelayed(1, 500);
                    }
                    break;
            }
            return true;
        }
    });

    @Override
    public int getLayoutId() {
        return R.layout.progress_image_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        progressImageView = findViewById(R.id.test_content);
    }

    @Override
    public void setListener() {
        super.setListener();
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(1);
            }
        });
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressImageView.reset();
                progress = 0;
                handler.removeMessages(1);
            }
        });
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.qq_send_pic_test);
    }

    private void startFake() {
        progressImageView.setProgress(0);
        handler.sendEmptyMessageDelayed(1, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler.removeCallbacksAndMessages(null);
    }
}
