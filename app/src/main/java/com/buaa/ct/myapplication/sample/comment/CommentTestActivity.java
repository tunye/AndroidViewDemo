package com.buaa.ct.myapplication.sample.comment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.ct.comment.CommentView;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

import java.net.URLDecoder;

public class CommentTestActivity extends BaseActivity {
    CommentView commentView;
    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.comment_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        textView = findViewById(R.id.test);
        commentView = findViewById(R.id.compose);
    }

    @Override
    public void setListener() {
        super.setListener();
        commentView.setOperationDelegate(new CommentView.OnComposeOperationDelegate() {
            @Override
            public void onSendText(String text) {
                try {
                    textView.setText(URLDecoder.decode(URLDecoder.decode(text, "UTF-8"), "UTF-8"));
                } catch (Exception e) {

                }
            }

            @Override
            public void onSendVoice(String file, int length) {
                if (length != 0)
                    Log.e("??", file);
                else
                    Toast.makeText(CommentTestActivity.this, "太短", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSendImageClicked(View v) {

            }

            @Override
            public void onSendLocationClicked(View v) {

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentTestActivity.this, EmojiTestActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (commentView.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
