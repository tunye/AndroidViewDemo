package com.buaa.ct.imageselector.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.util.SpringUtil;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.widget.PreviewViewPager;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/24.
 */
public class OnlyPreviewActivity extends CoreBaseActivity {
    public static final String EXTRA_PREVIEW_LIST = "previewList";
    public static final String EXTRA_INIT_POS = "extra_init_pos";
    public static final String EXTRA_SAVE_FUNC = "extra_save_func";
    private List<String> images;
    private int initPos;
    private PreviewViewPager viewPager;
    private View saveLocal;
    private View root;
    private SimpleFragmentAdapter fragmentAdapter;
    private boolean isShowBar = true;
    private boolean enableSave;

    public static void startPreview(Context context, String image) {
        List<String> images = new ArrayList<>();
        images.add(image);
        startPreview(context, images, 0);
    }

    public static void startPreview(Context context, List<String> images) {
        startPreview(context, images, 0);
    }

    public static void startPreview(Context context, List<String> images, int initPos) {
        startPreview(context, images, initPos, false);
    }

    public static void startPreview(Context context, List<String> images, int initPos, boolean enableSave) {
        Intent intent = new Intent(context, OnlyPreviewActivity.class);
        intent.putExtra(EXTRA_PREVIEW_LIST, (ArrayList) images);
        intent.putExtra(EXTRA_INIT_POS, initPos);
        intent.putExtra(EXTRA_SAVE_FUNC, enableSave);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void beforeSetLayout(Bundle saveBundle) {
        super.beforeSetLayout(saveBundle);
        images = (List<String>) getIntent().getSerializableExtra(EXTRA_PREVIEW_LIST);
        enableSave = getIntent().getBooleanExtra(EXTRA_SAVE_FUNC, false);
        initPos = getIntent().getIntExtra(EXTRA_INIT_POS, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        findViewById(android.R.id.content).setBackgroundColor(0x44000000);
        root = findViewById(R.id.preview_root);
        viewPager = findViewById(R.id.preview_pager);
        saveLocal = findViewById(R.id.save_local);
        findViewById(R.id.select_bar_layout).setVisibility(View.GONE);
    }

    @Override
    public void setListener() {
        super.setListener();
        saveLocal.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                fragmentAdapter.getItem(viewPager.getCurrentItem()).save();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (images.size() != 1) {
                    enableToolbarOper((position + 1) + "/" + images.size());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        fragmentAdapter = new SimpleFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(initPos);
        if (images.size() != 1) {
            enableToolbarOper((initPos + 1) + "/" + images.size());
        }
        title.setText(R.string.preview);
        if (enableSave) {
            saveLocal.setVisibility(View.VISIBLE);
        } else {
            saveLocal.setVisibility(View.GONE);
        }
    }

    public void scale(float dx, float dy, float ratio) {
        root.setTranslationX(dx);
        root.setTranslationY(dy);
        root.setScaleX(ratio);
        root.setScaleY(ratio);
        root.setAlpha(ratio);
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

    public class SimpleFragmentAdapter extends FragmentPagerAdapter {
        public SimpleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ImagePreviewFragment getItem(int position) {
            return ImagePreviewFragment.getInstance(images.get(position));
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }
}
