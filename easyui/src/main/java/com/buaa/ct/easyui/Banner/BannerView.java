package com.buaa.ct.easyui.Banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.buaa.ct.easyui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 102 on 2016/10/11.
 */

public class BannerView extends FrameLayout {
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
    private Align align;

    public enum Align {LEFT, CENTER, RIGHT}

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    bannerViewPager.setCurrentItem(currentItem, currentItem != 0);
                    break;
            }
        }
    };

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttr(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttr(context, attrs);
    }

    private void setAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.bannerview);
        selectItemColor = a.getColor(R.styleable.bannerview_banner_select_color, selectItemColor);
        unselectedItemColor = a.getColor(R.styleable.bannerview_banner_unselected_color, unselectedItemColor);
        shadowColor = a.getColor(R.styleable.bannerview_banner_shadow_color, shadowColor);
        isAutoStart = a.getBoolean(R.styleable.bannerview_banner_auto_start, true);
        int alignInt = a.getInt(R.styleable.bannerview_banner_align, 1);
        switch (alignInt) {
            case 0:
                align = Align.LEFT;
                break;
            case 1:
                align = Align.CENTER;
                break;
            case 2:
                align = Align.RIGHT;
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
        bannerViewPager.setPageTransformer(true, new DepthPageTransformer());

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
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(mDatas.get(mDatas.size() - 1));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        bannerImages.add(0, imageView);
        imageView = new ImageView(getContext());
        imageView.setImageResource(mDatas.get(0));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        bannerImages.add(imageView);
        bannerTitle.setText("第0张图");
        myAdapter.notifyDataSetChanged();

        if (isAutoStart)
            startAd();
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

    private Drawable createPoint(int color) {
        int size = (int) (getResources().getDisplayMetrics().density * 8 + 0.5f);
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

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % (bannerImages.size() - 2);
            handler.sendEmptyMessage(0);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (currentItem == bannerViewPager.getAdapter().getCount() - 1) {
                    bannerViewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    bannerViewPager.setCurrentItem(bannerViewPager.getAdapter().getCount() - 2, false);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setImageDrawable(createPoint(unselectedItemColor));
            oldPosition = currentItem % bannerData.size();
            bannerTitle.setText("第" + oldPosition + "张图");
            dots.get(oldPosition).setImageDrawable(createPoint(selectItemColor));
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return bannerImages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = bannerImages.get(position);
            container.addView(iv);
            // 在这个方法里面设置图片的点击事件
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "" + position % bannerData.size(), Toast.LENGTH_SHORT).show();
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(bannerImages.get(arg1));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
