package com.buaa.ct.comment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buaa.ct.comment.emoji.EmojiIcon;
import com.buaa.ct.comment.utils.CreateEmojiViewPagerData;
import com.buaa.ct.comment.viewpage.CirclePageIndicator;
import com.buaa.ct.comment.viewpager.EmojiViewPagerAdapter;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.GetAppColor;

public class EmojiView extends LinearLayout implements View.OnClickListener, EmojiViewPagerAdapter.OnClickEmojiListener {

    private ImageView mIvEmoji;
    private EditText mEtText;
    private ViewPager mViewPager;
    private EmojiViewPagerAdapter mPagerAdapter;
    private View mLyEmoji;


    public EmojiView(Context context) {
        this(context, null);
    }


    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private EmojiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public static void input(EditText editText, EmojiIcon emojiIcon) {
        if (editText == null || emojiIcon == null) {
            return;
        }
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojiIcon.getEmoji());
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end),
                    emojiIcon.getEmoji(), 0, emojiIcon.getEmoji().length());
        }
    }

    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }


    private void init(Context context) {
        inflate(context, R.layout.view_emoji, this);

        mIvEmoji = findViewById(R.id.iv_emoji);
        mIvEmoji.setOnClickListener(this);
        mLyEmoji = findViewById(R.id.ly_emoji);

        mViewPager = findViewById(R.id.view_pager);

        int emojiHeight = caculateEmojiPanelHeight();

        mPagerAdapter = new EmojiViewPagerAdapter(getContext(), CreateEmojiViewPagerData.create(),
                emojiHeight, this);
        mViewPager.setAdapter(mPagerAdapter);

        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setFillColor(GetAppColor.getInstance().getAppColor());
        indicator.setViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.iv_emoji) {
            if (mLyEmoji.getVisibility() == View.GONE) {
                tryShowEmojiPanel();
            } else {
                tryHideEmojiPanel();
            }
        }
    }

    private int caculateEmojiPanelHeight() {
        int mCurrentKeyboardHeight = RuntimeManager.getInstance().dip2px(210);

        mLyEmoji.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, mCurrentKeyboardHeight));

        int emojiPanelHeight = mCurrentKeyboardHeight - RuntimeManager.getInstance().dip2px(20);
        int emojiHeight = (emojiPanelHeight / 4);

        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, emojiPanelHeight);
        mViewPager.setLayoutParams(lp);
        if (mPagerAdapter != null) {
            mPagerAdapter.setEmojiHeight(emojiHeight);
        }
        return emojiHeight;
    }

    private void tryShowEmojiPanel() {
        hideKeyboard();
        showEmojiPanel();
    }

    private void tryHideEmojiPanel() {
        hideEmojiPanel();
        showKeyboard();
    }

    private void showEmojiPanel() {
        mLyEmoji.setVisibility(View.VISIBLE);
        mIvEmoji.setImageResource(R.drawable.btn_emoji_pressed);
    }

    private void hideEmojiPanel() {
        if (mLyEmoji.getVisibility() == View.VISIBLE) {
            mLyEmoji.setVisibility(View.GONE);
            mIvEmoji.setImageResource(R.drawable.btn_emoji_selector);
        }
    }

    private void hideKeyboard() {
        RuntimeManager.getInstance().hideSoftKeyboard(mEtText);
    }

    public void showKeyboard() {
        mEtText.requestFocus();
        RuntimeManager.getInstance().showSoftKeyboard(mEtText);
    }

    public void setmEtText(EditText mEtText) {
        this.mEtText = mEtText;
        mEtText.setOnClickListener(new INoDoubleClick() {
                @Override
                public void activeClick(View v) {
                if (mLyEmoji.isShown()) {
                    hideEmojiPanel();
                }
            }
        });
    }

    @Override
    public void onEmojiClick(EmojiIcon emoji) {
        input(mEtText, emoji);
    }

    @Override
    public void onDelete() {
        backspace(mEtText);
    }

    public void setTextHint(String text) {
        if (mEtText != null) {
            mEtText.setHint(text);
        }
    }

    public boolean onBackPressed() {
        if (mLyEmoji.isShown()) {
            hideEmojiPanel();
            return false;
        } else {
            return true;
        }
    }
}
