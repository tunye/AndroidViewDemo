package com.buaa.ct.easyui.progressimage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.buaa.ct.easyui.R;

import java.util.Random;

public class ProgressImageTestActivity extends AppCompatActivity {
    private Context context;
    private ProgressImageView progressImageView;
    private int progress;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, "启动模拟", Toast.LENGTH_SHORT).show();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_image_test);
        context = this;
        progressImageView = findViewById(R.id.test_content);
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
