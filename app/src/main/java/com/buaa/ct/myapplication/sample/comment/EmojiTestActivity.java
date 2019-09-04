package com.buaa.ct.myapplication.sample.comment;

import android.view.View;
import android.widget.EditText;

import com.buaa.ct.comment.EmojiView;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;


/**
 * Created by 10202 on 2016/4/26.
 */
public class EmojiTestActivity extends BaseActivity {
    EditText editText;
    EmojiView emojiView;
    View root;

    @Override
    public int getLayoutId() {
        return R.layout.comment_with_edittext;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        root = findViewById(R.id.root);
        editText = findViewById(R.id.edittext);
        emojiView = findViewById(R.id.emoji);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        emojiView.setmEtText(editText);
    }

    @Override
    public void onBackPressed() {
        if (emojiView.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
