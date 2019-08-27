package com.buaa.ct.easyui.progressimage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ProgressImageView extends AppCompatImageView {
    public static final int READY = 0;
    public static final int PROGRESS = 1;
    public static final int FINISH = 2;

    @IntDef({READY, PROGRESS, FINISH})
    @Retention(RetentionPolicy.SOURCE)
    @interface STATE {
    }

    private @STATE
    int state;
    private int outRadius = dp2px(50);
    private int inRadius = dp2px(40);
    private int round = outRadius - inRadius;

    private int progress = 0;
    private Paint paint = new Paint();
    private TextPaint textPaint;
    private Path path;
    private RectF rectF;
    private RadialGradient radialGradient;
    private PorterDuffXfermode porterDuffXfermode;

    private float currFinishAnimValue;
    private float currWaitingAnimValue;

    private ValueAnimator waittingAnimator;

    public ProgressImageView(Context context) {
        this(context, null);
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        state = READY;
        progress = 0;
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(dp2px(16));
        textPaint.setColor(Color.parseColor("#eeeeee"));
        paint.setAntiAlias(true);

        path = new Path();
        rectF = new RectF(0, 0, getWidth(), getHeight());
        radialGradient = new RadialGradient(0, 0, outRadius, new int[]{Color.TRANSPARENT, Color.WHITE, Color.WHITE, Color.TRANSPARENT}, new float[]{.1f, .4f, .8f, 1}, Shader.TileMode.CLAMP);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (state == READY) {
            startWaitingAnim();
        }
        if (progress < 100) {
            state = PROGRESS;
            postInvalidate();
        } else {
            state = FINISH;
            waittingAnimator.pause();
            waittingAnimator = null;
            startFinishAnim();
        }
    }

    private void startFinishAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1200);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currFinishAnimValue = (float) animation.getAnimatedValue();
                postInvalidateOnAnimation();
            }
        });
        valueAnimator.start();
    }

    private void startWaitingAnim() {
        waittingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(700);
        waittingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waittingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waittingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currWaitingAnimValue = (float) animation.getAnimatedValue();
                postInvalidateOnAnimation();
            }
        });
        waittingAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        canvas.save();
        path.reset();
        path.addRoundRect(rectF, round, round, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#99000000"));
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        switch (state) {
            case PROGRESS:
                canvas.drawPaint(paint);
                int sc = canvas.saveLayer(-outRadius, -outRadius, outRadius, outRadius, paint, Canvas.ALL_SAVE_FLAG);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                paint.setShader(radialGradient);
                paint.setAlpha((int) (currWaitingAnimValue * 255));
                canvas.drawCircle(0, 0, inRadius + round * currWaitingAnimValue, paint);
                paint.setXfermode(porterDuffXfermode);
                paint.setShader(null);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(0, 0, inRadius, paint);
                paint.setXfermode(null);
                canvas.drawText(getCurrProgress(), -textPaint.measureText(getCurrProgress()) / 2, (textPaint.descent() - textPaint.ascent()) / 2 - textPaint.descent(), textPaint);
                canvas.restoreToCount(sc);
                break;
            case FINISH:
                sc = canvas.saveLayer(-getWidth() / 2, -getHeight() / 2, getWidth() / 2, getHeight() / 2, paint, Canvas.ALL_SAVE_FLAG);
                canvas.drawPaint(paint);
                double maxRadius = Math.sqrt(Math.pow(getWidth() * 1f, 2f)) + Math.sqrt(Math.pow(getHeight() * 1f, 2f)) / 2;
                paint.setXfermode(porterDuffXfermode);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(0, 0, (float) (outRadius + (maxRadius - outRadius) * currFinishAnimValue), paint);
                paint.setXfermode(null);
                canvas.restoreToCount(sc);
                break;
        }
        canvas.restore();
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 100);
    }

    public void reset() {
        init();
        postInvalidate();
    }

    private String getCurrProgress() {
        return String.valueOf(progress);
    }

    private int dp2px(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
