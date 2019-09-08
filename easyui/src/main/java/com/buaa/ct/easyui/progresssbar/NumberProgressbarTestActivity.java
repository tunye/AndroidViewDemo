package com.buaa.ct.easyui.progresssbar;

import android.os.Handler;
import android.os.Message;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;


public class NumberProgressbarTestActivity extends CoreBaseActivity implements OnProgressBarListener {
    private NumberProgressBar bnp;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    bnp.incrementProgressBy(1);
                    handler.sendEmptyMessageDelayed(0, 100);
                    break;
                case 1:
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

    @Override
    public int getLayoutId() {
        return R.layout.number_progressbar_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        bnp = findViewById(R.id.numberbar1);
    }

    @Override
    public void setListener() {
        super.setListener();
        bnp.setOnProgressBarListener(this);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.progress_test);
    }

    @Override
    public void onActivityResumed() {
        super.onActivityResumed();
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
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            CustomToast.getInstance().showToast("完成");
            handler.sendEmptyMessage(2);
            handler.sendEmptyMessageDelayed(1, 2000);
        }
    }
}