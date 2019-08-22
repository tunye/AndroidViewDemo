package com.buaa.ct.easyui.progresssbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.buaa.ct.easyui.R;


public class NumberProgressbarTestActivity extends AppCompatActivity implements OnProgressBarListener {
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("aaa", "步进");
                    bnp.incrementProgressBy(1);
                    handler.sendEmptyMessageDelayed(0, 100);
                    break;
                case 1:
                    Log.e("aaa", "重置");
                    bnp.setProgress(0);
                    handler.sendEmptyMessageDelayed(0, 1000);
                    break;
                case 2:
                    handler.removeMessages(0);
                    break;
            }
            return true;
        }
    });

    private NumberProgressBar bnp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_progressbar_test);

        bnp = findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.sendEmptyMessage(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage(2);
            handler.sendEmptyMessageDelayed(1, 2000);
        }
    }
}