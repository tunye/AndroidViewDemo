package com.buaa.ct.imageselector.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.util.SpringUtil;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.imageselector.MediaListManager;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.model.LocalMedia;
import com.buaa.ct.imageselector.widget.PreviewViewPager;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/24.
 */
public class ImagePreviewActivity extends CoreBaseActivity {
    public static final int REQUEST_PREVIEW = 68;
    public static final String EXTRA_PREVIEW_LIST = "previewList";
    public static final String EXTRA_PREVIEW_SELECT_LIST = "previewSelectList";
    public static final String EXTRA_MAX_SELECT_NUM = "maxSelectNum";
    public static final String EXTRA_POSITION = "position";

    public static final String OUTPUT_LIST = "outputList";
    public static final String OUTPUT_ISDONE = "isDone";

    private View selectBarLayout;
    private View root;
    private CheckBox checkboxSelect;
    private PreviewViewPager viewPager;

    private int position;
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();

    private boolean isShowBar = true;

    public static void startPreview(Activity context, List<LocalMedia> selectImages, int maxSelectNum, int position) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(EXTRA_PREVIEW_SELECT_LIST, (ArrayList) selectImages);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_MAX_SELECT_NUM, maxSelectNum);
        context.startActivityForResult(intent, REQUEST_PREVIEW);
        context.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        root = findViewById(R.id.preview_root);
        checkboxSelect = findViewById(R.id.checkbox_select);
        viewPager = findViewById(R.id.preview_pager);
        selectBarLayout = findViewById(R.id.select_bar_layout);
    }

    @Override
    public void setListener() {
        super.setListener();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                title.setText(position + 1 + "/" + images.size());
                onImageSwitch(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        checkboxSelect.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                boolean isChecked = checkboxSelect.isChecked();
                if (selectImages.size() >= maxSelectNum && isChecked) {
                    CustomToast.getInstance().showToast(getString(R.string.message_max_num, maxSelectNum), Toast.LENGTH_LONG);
                    checkboxSelect.setChecked(false);
                    return;
                }
                LocalMedia image = images.get(viewPager.getCurrentItem());
                if (isChecked) {
                    setCheckBoxCheckedIcon();
                    selectImages.add(image);
                } else {
                    setCheckBoxUnCheckedIcon();
                    for (LocalMedia media : selectImages) {
                        if (media.getPath().equals(image.getPath())) {
                            selectImages.remove(media);
                            break;
                        }
                    }
                }
                onSelectNumChange();
            }
        });
        toolbarOper.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                if (selectImages.isEmpty()) {
                    CustomToast.getInstance().showToast(R.string.message_min_num);
                } else {
                    onDoneClick(true);
                }
            }
        });
    }

    @Override
    public void beforeSetLayout(Bundle saveBundle) {
        super.beforeSetLayout(saveBundle);
        images = MediaListManager.getInstance().getMediaList();
        selectImages = (List<LocalMedia>) getIntent().getSerializableExtra(EXTRA_PREVIEW_SELECT_LIST);
        maxSelectNum = getIntent().getIntExtra(EXTRA_MAX_SELECT_NUM, 9);
        position = getIntent().getIntExtra(EXTRA_POSITION, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) title.getLayoutParams();
        layoutParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        layoutParams.setMargins(RuntimeManager.getInstance().dip2px(44), 0, 0, 0);
        title.setText((position + 1) + "/" + images.size());

        enableToolbarOper(R.string.done);
        onSelectNumChange();
        onImageSwitch(position);
        viewPager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(position);
    }


    @Override
    public void onBackPressed() {
        onDoneClick(false);
    }

    public void onSelectNumChange() {
        boolean enable = selectImages.size() != 0;
        if (enable) {
            enableToolbarOper(getString(R.string.done_num, selectImages.size(), maxSelectNum));
        } else {
            enableToolbarOper(R.string.done);
        }
    }

    public void setCheckBoxCheckedIcon() {
        Drawable selectDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_checked));
        DrawableCompat.setTint(selectDrawable, GetAppColor.getInstance().getAppColor());
        checkboxSelect.setButtonDrawable(selectDrawable);
    }

    public void setCheckBoxUnCheckedIcon() {
        checkboxSelect.setButtonDrawable(R.drawable.ic_check);
    }

    public void onImageSwitch(int position) {
        checkboxSelect.setChecked(isSelected(images.get(position)));
        if (checkboxSelect.isSelected()) {
            setCheckBoxCheckedIcon();
        } else {
            setCheckBoxUnCheckedIcon();
        }
    }

    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    public void scale(float dx, float dy, float ratio) {
        root.setTranslationX(dx);
        root.setTranslationY(dy);
        root.setScaleX(ratio);
        root.setScaleY(ratio);
        root.setAlpha(ratio);
        if (ratio == 0) {
            viewPager.setLock(false);
        } else {
            viewPager.setLock(true);
        }
    }

    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public void switchBarVisibility() {
        SpringUtil.getInstance().addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                toolBarLayout.setTranslationY((float) spring.getCurrentValue());
                float alpha = 1 - Math.abs(toolBarLayout.getTranslationY() / toolBarLayout.getMeasuredHeight());
                toolBarLayout.setAlpha(alpha);
                selectBarLayout.setAlpha(alpha);
                selectBarLayout.setTranslationY((float) (Math.abs(spring.getCurrentValue()) * selectBarLayout.getMeasuredHeight() / toolBarLayout.getMeasuredHeight()));
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
                SpringUtil.getInstance().destory();
            }
        });
        SpringUtil.getInstance().setCurrentValue(toolBarLayout.getTranslationY());
        SpringUtil.getInstance().setEndValue(isShowBar ? -toolBarLayout.getMeasuredHeight() : 0);

        if (isShowBar) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
        isShowBar = !isShowBar;
    }

    public void onDoneClick(boolean isDone) {
        Intent intent = new Intent();
        intent.putExtra(OUTPUT_LIST, (ArrayList) selectImages);
        intent.putExtra(OUTPUT_ISDONE, isDone);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class SimpleFragmentAdapter extends FragmentPagerAdapter {
        public SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImagePreviewFragment.getInstance(images.get(position).getPath());
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }
}
