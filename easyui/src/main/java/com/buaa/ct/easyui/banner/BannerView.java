package com.buaa.ct.easyui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.ThreadUtils;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 102 on 2016/10/11.
 */

public class BannerView extends FrameLayout {
    public final static int LEFT = 1;
    public final static int RIGHT = 2;
    public final static int CENTER = 0;
    private ViewPager bannerViewPager;
    private MyAdapter myAdapter;
    private TextView bannerTitle;
    private LinearLayout dotLayout;
    private List<ImageView> bannerImages;
    private List<ImageView> dots;
    private List<Integer> bannerData;
    private int currentItem = 0;
    private boolean isLooping = false;
    private ScheduledExecutorService scheduledExecutorService;
    private int selectItemColor = 0xffffffff, unselectedItemColor = 0xff808080;
    private int shadowColor = 0x66000000;
    private boolean isAutoStart;
    private @Align
    int align;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttr(context, attrs);
    }

    private void setAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        selectItemColor = a.getColor(R.styleable.BannerView_banner_select_color, selectItemColor);
        unselectedItemColor = a.getColor(R.styleable.BannerView_banner_unselected_color, unselectedItemColor);
        shadowColor = a.getColor(R.styleable.BannerView_banner_shadow_color, shadowColor);
        isAutoStart = a.getBoolean(R.styleable.BannerView_banner_auto_start, true);
        int alignInt = a.getInt(R.styleable.BannerView_banner_align, 1);
        switch (alignInt) {
            case 0:
                align = LEFT;
                break;
            case 1:
                align = CENTER;
                break;
            case 2:
                align = RIGHT;
                break;
        }
        a.recycle();
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.banner, this, true);
        bannerViewPager = root.findViewById(R.id.banner_vp);
        bannerTitle = root.findViewById(R.id.banner_title);
        View bannerShadow = root.findViewById(R.id.banner_shadow);
        bannerShadow.setBackgroundColor(shadowColor);
        dotLayout = root.findViewById(R.id.banner_dot);

        dots = new ArrayList<>();
        bannerImages = new ArrayList<>();
        myAdapter = new MyAdapter();
        bannerViewPager.setAdapter(myAdapter);// 设置填充ViewPager页面的适配器
        bannerViewPager.addOnPageChangeListener(new MyPageChangeListener());
        bannerViewPager.setPageTransformer(false, new DepthPageTransformer());

        LayoutParams params;
        switch (align) {
            case LEFT:
                bannerTitle.setVisibility(VISIBLE);
                params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.gravity = (Gravity.START | Gravity.CENTER_VERTICAL);
                dotLayout.setLayoutParams(params);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.gravity = (Gravity.END | Gravity.CENTER_VERTICAL);
                bannerTitle.setLayoutParams(params);
                break;
            case RIGHT:
                bannerTitle.setVisibility(VISIBLE);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.gravity = (Gravity.END | Gravity.CENTER_VERTICAL);
                dotLayout.setLayoutParams(params);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.gravity = (Gravity.START | Gravity.CENTER_VERTICAL);
                bannerTitle.setLayoutParams(params);
                break;
            case CENTER:
                bannerTitle.setVisibility(GONE);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                dotLayout.setLayoutParams(params);
                break;
        }
    }

    public void initData(List<Integer> mDatas) {
        bannerData = mDatas;

        for (int count = 0; count < mDatas.size(); count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(8, 0, 8, 0);
            if (dots.isEmpty())
                pointView.setImageDrawable(createPoint(selectItemColor));
            else
                pointView.setImageDrawable(createPoint(unselectedItemColor));
            dots.add(pointView);
            dotLayout.addView(pointView);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(mDatas.get(count));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bannerImages.add(imageView);
        }
        bannerTitle.setText("第0张图");
        myAdapter.notifyDataSetChanged();
    }

    public void startAd() {
        if (!isLooping && bannerData.size() > 1) {
            isLooping = true;
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5, TimeUnit.SECONDS);
        }
    }

    public void stopAd() {
        if (isLooping) {
            isLooping = false;
            scheduledExecutorService.shutdown();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            startAd();
        } else {
            stopAd();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAd();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoStart)
            startAd();
    }

    public void setSelectItemColor(int selectItemColor) {
        this.selectItemColor = selectItemColor;
    }

    private Drawable createPoint(int color) {
        int size = RuntimeManager.getInstance().dip2px(8);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        canvas.drawCircle(size * 1.0f / 2, size * 1.0f / 2, size * 1.0f / 2, paint);
        drawable.draw(canvas);

        return drawable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startAd();
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            stopAd();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {LEFT, RIGHT, CENTER})
    @interface Align {

    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            ThreadUtils.postOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerViewPager.setCurrentItem(currentItem + 1, true);
                }
            });
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            int oldRealPosition = currentItem % bannerData.size();
            currentItem = position;
            int nowRealPosition = currentItem % bannerData.size();
            dots.get(oldRealPosition).setImageDrawable(createPoint(unselectedItemColor));
            bannerTitle.setText("第" + nowRealPosition + "张图");
            dots.get(nowRealPosition).setImageDrawable(createPoint(selectItemColor));
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final int realPos = position % bannerImages.size();
            ImageView iv = bannerImages.get(realPos);
            container.addView(iv);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new INoDoubleClick() {

                @Override
                public void activeClick(View v) {
                    CustomToast.getInstance().showToast("第" + realPos + "张图");
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(@NonNull View arg0, int arg1, @NonNull Object arg2) {
            ((ViewPager) arg0).removeView(bannerImages.get(arg1 % bannerImages.size()));
        }

        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
