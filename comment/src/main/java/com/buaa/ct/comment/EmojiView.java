package com.buaa.ct.comment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.buaa.ct.comment.emoji.EmojiIcon;
import com.buaa.ct.comment.emoji.EmojiViewPagerAdapter;
import com.buaa.ct.comment.emoji.People;
import com.buaa.ct.comment.util.SystemUtil;
import com.buaa.ct.comment.viewpage.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class EmojiView extends LinearLayout implements View.OnClickListener, EmojiViewPagerAdapter.OnClickEmojiListener {

    private ImageView mIvEmoji;
    private EditText mEtText;
    private OnComposeOperationDelegate mDelegate;
    private ViewPager mViewPager;
    private EmojiViewPagerAdapter mPagerAdapter;
    private int mCurrentKeyboardHeight;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private EmojiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

        mIvEmoji = (ImageView) findViewById(R.id.iv_emoji);
        mIvEmoji.setOnClickListener(this);
        mLyEmoji = findViewById(R.id.ly_emoji);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        int emojiHeight = caculateEmojiPanelHeight();

        EmojiIcon[] emojis = People.DATA;
        List<List<EmojiIcon>> pagers = new ArrayList<>();
        List<EmojiIcon> es = null;
        int size = 0;
        boolean justAdd = false;
        for (EmojiIcon ej : emojis) {
            if (size == 0) {
                es = new ArrayList<>();
            }
            if (size == 27) {
                es.add(new EmojiIcon(""));
            } else {
                es.add(ej);
            }
            size++;
            if (size == 28) {
                pagers.add(es);
                size = 0;
                justAdd = true;
            } else {
                justAdd = false;
            }
        }
        if (!justAdd && es != null) {
            int exSize = 28 - es.size();
            for (int i = 0; i < exSize; i++) {
                es.add(new EmojiIcon(""));
            }
            pagers.add(es);
        }

        mPagerAdapter = new EmojiViewPagerAdapter(getContext(), pagers,
                emojiHeight, this);
        mViewPager.setAdapter(mPagerAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setFillColor(0xff45B91C);
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
        mCurrentKeyboardHeight = (int) SystemUtil.dpToPixel(210);

        mLyEmoji.setLayoutParams(new RelativeLayout
                .LayoutParams(LayoutParams.MATCH_PARENT, mCurrentKeyboardHeight));

        int emojiPanelHeight = (int) (mCurrentKeyboardHeight - SystemUtil
                .dpToPixel(20));
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
        SystemUtil.hideSoftKeyboard(mEtText);
    }

    public void showKeyboard() {
        mEtText.requestFocus();
        SystemUtil.showSoftKeyboard(mEtText);
    }

    public void setmEtText(EditText mEtText) {
        this.mEtText = mEtText;
        mEtText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public interface OnComposeOperationDelegate {
        void onSendText(String text);

        void onSendVoice(String file, int length);

        void onSendImageClicked(View v);

        void onSendLocationClicked(View v);
    }
}
