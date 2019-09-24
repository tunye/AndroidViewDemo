package com.buaa.ct.easyui.FallStar;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class FallingView extends View {
    private static final int defaultWidth = 600;
    private static final int defaultHeight = 1000;
    private static final long INTERVAL_TIME = 1000 / 30;
    private List<FallObject> fallObjects;
    private int viewWidth;
    private int viewHeight;
    private boolean stopped;
    // 重绘
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!fallObjects.isEmpty()) {
                for (int i = 0; i < fallObjects.size(); i++) {
                    fallObjects.get(i).moveObject();
                }
            }
            invalidate();
            if (!stopped) {
                postDelayed(runnable, INTERVAL_TIME);
            }
        }
    };

    public FallingView(Context context) {
        this(context, null);
    }

    public FallingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        fallObjects = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultHeight, heightMeasureSpec);
        int width = measureSize(defaultWidth, widthMeasureSpec);
        setMeasuredDimension(width, height);

        viewWidth = width;
        viewHeight = height;
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!fallObjects.isEmpty()) {
            for (int i = 0; i < fallObjects.size(); i++) {
                //然后进行绘制
                fallObjects.get(i).drawObject(canvas);
            }
        }
    }

    public void refresh() {
        if (!fallObjects.isEmpty()) {
            for (int i = 0; i < fallObjects.size(); i++) {
                //然后进行绘制
                fallObjects.get(i).refresh();
            }
        }
    }

    public void startFalling() {
        stopped = false;
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacksAndMessages(runnable);
        }
        post(runnable);
    }

    public void stopFalling() {
        stopped = true;
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacksAndMessages(runnable);
        }
    }

    /**
     * 向View添加下落物体对象
     *
     * @param builder 下落物体对象构造器
     * @param num
     */
    public void addFallObject(final int num, final FallObject.Builder builder) {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (viewWidth > 0 && viewHeight > 0) {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    for (int i = 0; i < num; i++) {
                        FallObject newFallObject = new FallObject(builder, viewWidth, viewHeight);
                        fallObjects.add(newFallObject);
                    }
                    invalidate();
                }
                return true;
            }
        });
    }
}