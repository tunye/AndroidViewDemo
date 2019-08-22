package com.buaa.ct.comment.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.buaa.ct.comment.ContextManager;
import com.buaa.ct.comment.EmojiView;
import com.buaa.ct.comment.R;


/**
 * Created by 10202 on 2016/4/26.
 */
public class EmojiTestActivity extends AppCompatActivity {
    EditText editText;
    EmojiView emojiView;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextManager.setInstance(this);
        setContentView(R.layout.comment_with_edittext);
        root = findViewById(R.id.root);
        editText = (EditText) findViewById(R.id.edittext);
        emojiView = (EmojiView) findViewById(R.id.emoji);
        emojiView.setmEtText(editText);
    }

    @Override
    public void onBackPressed() {
        if (emojiView.onBackPressed()) {
            super.onBackPressed();
        }
    }

}
