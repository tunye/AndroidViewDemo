package com.buaa.ct.myapplication.sample.copyboard;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;


public final class CopyboardTestActivity extends BaseActivity {

    private final static String KEY_CONTENT = "content";
    private TextView mTextView;

    public static void startForContent(Context context, String content) {
        Intent intent = new Intent(context, CopyboardTestActivity.class);
        intent.putExtra(KEY_CONTENT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.copy_board_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mTextView = findViewById(R.id.text_view);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        Intent intent = getIntent();
        tryToShowContent(intent);
        ListenClipboardService.start(this);
        title.setText(R.string.test_copyboard);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        tryToShowContent(intent);
    }

    private void tryToShowContent(Intent intent) {
        String content = intent.getStringExtra(KEY_CONTENT);
        if (!TextUtils.isEmpty(content)) {
            mTextView.setText(content);
        }
    }
}