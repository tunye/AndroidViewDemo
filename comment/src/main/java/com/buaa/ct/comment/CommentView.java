package com.buaa.ct.comment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.buaa.ct.comment.emoji.EmojiIcon;
import com.buaa.ct.comment.emoji.EmojiViewPagerAdapter;
import com.buaa.ct.comment.emoji.EmojiconEditText;
import com.buaa.ct.comment.emoji.People;
import com.buaa.ct.comment.emoji.RecordButton;
import com.buaa.ct.comment.emoji.SoftKeyboardStateHelper;
import com.buaa.ct.comment.util.SystemUtil;
import com.buaa.ct.comment.viewpage.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class CommentView extends LinearLayout implements View.OnClickListener, RecordButton.OnFinishedRecordListener, SoftKeyboardStateHelper.SoftKeyboardStateListener, EmojiViewPagerAdapter.OnClickEmojiListener {

    private ImageView mIvEmoji;
    private ImageView mIvVoiceText;
    private Animation mShowAnim, mDismissAnim, mShowMoreAnim, mDismissMoreAnim;
    private ImageView mIvMore;
    private Button mBtnSend;
    private RecordButton mBtnVoice;
    private EmojiconEditText mEtText;

    private OnComposeOperationDelegate mDelegate;
    private ViewPager mViewPager;
    private EmojiViewPagerAdapter mPagerAdapter;
    private View mLyOpt, mLyEmoji;
    private boolean mIsKeyboardVisible;
    private boolean showVoice = true;
    private boolean showMore = true;
    private boolean voiceOnly = false;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s) || s.toString().trim().equals("")) {//
                if (mBtnSend.getVisibility() == View.VISIBLE) {
                    dismissSendButton();
                }
            } else {
                if (mBtnSend.getVisibility() != View.VISIBLE) {
                    showSendButton();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public CommentView(Context context) {
        this(context, null);
    }


    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private CommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
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

    private void showSendButton() {
        mIvMore.clearAnimation();
        mIvMore.startAnimation(mDismissMoreAnim);
        mShowAnim.reset();
        mBtnSend.clearAnimation();
        mBtnSend.startAnimation(mShowAnim);
    }

    private void dismissSendButton() {
        mIvMore.clearAnimation();
        mIvMore.startAnimation(mShowMoreAnim);
        mBtnSend.clearAnimation();
        mBtnSend.startAnimation(mDismissAnim);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ComposeView);
        showVoice = typedArray.getBoolean(R.styleable.ComposeView_showVoice, true);
        showMore = typedArray.getBoolean(R.styleable.ComposeView_showMore, true);
        voiceOnly = typedArray.getBoolean(R.styleable.ComposeView_voiceOnly, false);
        typedArray.recycle();
        initAnim(context);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_compose, this);

        mIvEmoji = (ImageView) findViewById(R.id.iv_emoji);
        mIvEmoji.setOnClickListener(this);
        mEtText = (EmojiconEditText) findViewById(R.id.et_text);
        mEtText.addTextChangedListener(mTextWatcher);
        mEtText.setOnClickListener(this);
        mIvMore = (ImageView) findViewById(R.id.iv_more);
        mIvMore.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnVoice = (RecordButton) findViewById(R.id.btn_voice);
        mBtnVoice.setRecorderCallback(this);

        mIvVoiceText = (ImageView) findViewById(R.id.iv_voice_text);
        mIvVoiceText.setOnClickListener(this);
        if (!showVoice) {
            mIvVoiceText.setVisibility(View.GONE);
            mIvMore.setVisibility(View.GONE);
        }
        if (!showMore) {
            mIvMore.setVisibility(View.GONE);
        }

        findViewById(R.id.iv_opt_picture).setOnClickListener(this);

        mLyEmoji = findViewById(R.id.ly_emoji);
        mLyOpt = findViewById(R.id.ly_opt);

        SoftKeyboardStateHelper mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext()).getWindow()
                .getDecorView());
        mKeyboardHelper.addSoftKeyboardStateListener(this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        int emojiHeight = calculateEmiliPanelHeight();

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
        if (voiceOnly) {
            mIvVoiceText.setVisibility(View.GONE);
            mIvMore.setVisibility(View.GONE);
            changeToVoice();
        }
    }

    private void initAnim(Context context) {
        mShowMoreAnim = AnimationUtils.loadAnimation(context, R.anim.chat_show_more_button);
        mDismissMoreAnim = AnimationUtils.loadAnimation(context, R.anim.chat_show_more_button);

        mShowAnim = AnimationUtils.loadAnimation(context, R.anim.chat_show_send_button);
        mShowAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mBtnSend.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mDismissAnim = AnimationUtils.loadAnimation(context, R.anim.chat_dismiss_send_button);
        mDismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnSend.setVisibility(View.GONE);
                if (showMore) {
                    mIvMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_send) {
            if (mDelegate != null) {
                mDelegate.onSendText(mEtText.getText().toString());
            }
        } else if (id == R.id.iv_voice_text) {
            if (mBtnVoice.getVisibility() == View.VISIBLE) {
                // Recording,Switch to InputMode
                changeToInput();
                showKeyboard();
            } else {
                // Switch to VoiceMode
                changeToVoice();
                hideEmojiOptAndKeyboard();
            }
        } else if (id == R.id.iv_more) {
            if (mBtnVoice.getVisibility() == View.VISIBLE) {
                changeToInput();
            }
            hideEmojiPanel();
            if (mLyOpt.getVisibility() == View.GONE) {
                tryShowOptPanel();
            } else {
                tryHideOptPanel();
            }
        } else if (id == R.id.iv_emoji) {
            hideOptPanel();
            if (mLyEmoji.getVisibility() == View.GONE) {
                tryShowEmojiPanel();
            } else {
                tryHideEmojiPanel();
            }
        } else if (id == R.id.iv_opt_picture) {
            if (mDelegate != null) {
                mDelegate.onSendImageClicked(v);
            }
            hideEmojiOptAndKeyboard();
        } else if (id == R.id.et_text) {
            hideEmojiPanel();
            hideOptPanel();
        }
    }

    private void changeToVoice() {
        mBtnVoice.setVisibility(View.VISIBLE);
        mEtText.setVisibility(View.GONE);
        mIvEmoji.setVisibility(View.GONE);
        mIvVoiceText.setImageResource(R.drawable.btn_to_text_selector);
    }

    private void changeToInput() {
        mBtnVoice.setVisibility(View.GONE);
        mEtText.setVisibility(View.VISIBLE);
        mIvEmoji.setVisibility(View.VISIBLE);
        mIvVoiceText.setImageResource(R.drawable.btn_to_voice_selector);
    }

    private int calculateEmiliPanelHeight() {
        int mCurrentKeyboardHeight = (int) SystemUtil.dpToPixel(180);

        mLyOpt.setLayoutParams(new RelativeLayout
                .LayoutParams(LayoutParams.MATCH_PARENT, mCurrentKeyboardHeight));

        int emojiPanelHeight = (int) (mCurrentKeyboardHeight - SystemUtil
                .dpToPixel(20));
        int emojiHeight = emojiPanelHeight / 4;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, emojiPanelHeight);
        mViewPager.setLayoutParams(lp);
        if (mPagerAdapter != null) {
            mPagerAdapter.setEmojiHeight(emojiHeight);
        }
        return emojiHeight;
    }

    private void tryShowEmojiPanel() {
        if (mIsKeyboardVisible) {
            hideKeyboard();
        }
        showEmojiPanel();
    }

    private void tryHideEmojiPanel() {
        if (!mIsKeyboardVisible) {
            showKeyboard();
        }
        hideEmojiPanel();
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

    public void hideEmojiOptAndKeyboard() {
        hideEmojiPanel();
        hideOptPanel();
        SystemUtil.hideSoftKeyboard(mEtText);
    }

    private void tryHideOptPanel() {
        if (!mIsKeyboardVisible) {
            showKeyboard();
        }
        hideOptPanel();
    }

    private void tryShowOptPanel() {
        if (mIsKeyboardVisible) {
            hideKeyboard();
        }
        showOptPanel();
    }

    private void showOptPanel() {
        mLyOpt.setVisibility(View.VISIBLE);
    }

    private void hideOptPanel() {
        if (mLyOpt.getVisibility() == View.VISIBLE) {
            mLyOpt.setVisibility(View.GONE);
        }
    }

    private void hideKeyboard() {
        SystemUtil.hideSoftKeyboard(mEtText);
    }

    public void showKeyboard() {
        mEtText.requestFocus();
        SystemUtil.showSoftKeyboard(mEtText);
    }

    public EmojiconEditText getmEtText() {
        return mEtText;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        mIsKeyboardVisible = true;
        hideEmojiPanel();
        hideOptPanel();
    }

    @Override
    public void onSoftKeyboardClosed() {
        mIsKeyboardVisible = false;
    }

    @Override
    public void onEmojiClick(EmojiIcon emoji) {
        input(mEtText, emoji);
    }

    @Override
    public void onDelete() {
        backspace(mEtText);
    }

    @Override
    public void onFinishedRecord(String audioPath, int length) {
        if (mDelegate != null) {
            mDelegate.onSendVoice(audioPath, length);
        }
    }

    public void setOperationDelegate(OnComposeOperationDelegate delegate) {
        mDelegate = delegate;
    }

    public void clearText() {
        if (mEtText != null) {
            mEtText.setText("");
            mEtText.setHint("");
        }
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
        } else if (mLyOpt.isShown()) {
            hideOptPanel();
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
