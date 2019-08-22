package com.buaa.ct.comment.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.ct.comment.CommentView;
import com.buaa.ct.comment.ContextManager;
import com.buaa.ct.comment.R;

import java.net.URLDecoder;

public class CommentTestActivity extends AppCompatActivity {
    CommentView commentView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextManager.setInstance(this);
        setContentView(R.layout.comment_test);
        textView =  findViewById(R.id.test);
        commentView =  findViewById(R.id.compose);
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
