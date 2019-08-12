package com.buaa.ct.easyui.DraggableFlag;

/**
 * Created by 琪 on 2015/8/24.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;

import com.buaa.ct.easyui.R;

public class DraggableFlagView extends View {
    public static final int MIN_RADIUS = 2;
    private Context context;
    private OnDraggableFlagViewListener onDraggableFlagViewListener;

    private int textColor = Color.WHITE;
    private int patientColor = Color.RED;
    private int originRadius; // 初始的圆的半径
    private int maxMoveLength; // 最大的移动拉长距离
    private int textSize = 12;
    private String text = "0"; // 正常状态下显示的文字
    private int animInterval = 600;//ms

    private int originWidth;
    private int originHeight;

    private boolean isFirst = true;
    private boolean isTouched; // 是否是触摸状态
    private boolean isArrivedMaxMoved; // 达到了最大的拉长距离（松手可以触发事件）

    private int curRadius; // 当前点的半径
    private Path path = new Path();
    private Point startPoint = new Point();
    private Point endPoint = new Point();
    private RelativeLayout.LayoutParams originLp; // 实际的layoutparams
    private RelativeLayout.LayoutParams newLp; // 触摸时候的LayoutParams

    private Paint paint; // 绘制圆形图形
    private Paint textPaint; // 绘制圆形图形

    private float downX = Float.MAX_VALUE;
    private float downY = Float.MAX_VALUE;
    private int[] location;
    private int[] exposeDrawable = {R.drawable.tips_bubble_idp, R.drawable.tips_bubble_idq, R.drawable.tips_bubble_idr, R.drawable.tips_bubble_ids, R.drawable.tips_bubble_idt};

    private Triangle triangle = new Triangle();
    /**
     * 回滚状态动画
     */
    private ValueAnimator rollBackAnim, exposeAnim;

    public DraggableFlagView(Context context) {
        this(context, null);
    }

    public DraggableFlagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableFlagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.draggableflag);
            maxMoveLength = a.getDimensionPixelSize(R.styleable.draggableflag_max_move, maxMoveLength);
            if (maxMoveLength == 0) {
                maxMoveLength = getDeviceHeight(context) / 6;
            }
            patientColor = a.getColor(R.styleable.draggableflag_roundcolor, patientColor);
            textColor = a.getColor(R.styleable.draggableflag_textcolor, textColor);
            text = a.getString(R.styleable.draggableflag_text);
            if (text == null || "".equals(text)) {
                text = "0";
            }
            textSize = a.getInt(R.styleable.draggableflag_textsize, textSize);
            animInterval = a.getInt(R.styleable.draggableflag_anim_interval, 600);
            a.recycle();
        } else {
            textColor = Color.WHITE;
            patientColor = Color.RED;
            textSize = 12;
            maxMoveLength = getDeviceHeight(context) / 6;
            text = "0";
            animInterval = 600;
        }
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setBackgroundColor(Color.TRANSPARENT);
        // 设置绘制flag的paint
        paint = new Paint();
        paint.setColor(patientColor);
        paint.setAntiAlias(true);

        // 设置绘制文字的paint
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(ScreenUtil.sp2px(context, textSize));
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (isFirst && w > 0 && h > 0) {
            isFirst = false;

            originWidth = w;
            originHeight = h;

            originRadius = Math.min(originWidth, originHeight) / 2;
            curRadius = originRadius;

            refreshStartPoint();

            ViewGroup.LayoutParams lp = this.getLayoutParams();
            if (RelativeLayout.LayoutParams.class.isAssignableFrom(lp.getClass())) {
                originLp = (RelativeLayout.LayoutParams) lp;
            }
            newLp = new RelativeLayout.LayoutParams(lp.width, lp.height);
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        refreshStartPoint();
    }

    /**
     * 修改layoutParams后，需要重新设置startPoint
     */
    private void refreshStartPoint() {
        location = new int[2];

        this.getLocationInWindow(location);
        try {
            location[1] = location[1] - ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        } catch (Exception ex) {
        }
        startPoint.set(location[0] - 48, location[1] + getMeasuredHeight() - 48);//排除padding
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        int startCircleX = 0, startCircleY = 0;
        if (isTouched) { // 触摸状态
            startCircleX = startPoint.x + originRadius;
            startCircleY = startPoint.y - originRadius;
            // 绘制原来的圆形（触摸移动的时候半径会不断变化）
            canvas.drawCircle(startCircleX, startCircleY, curRadius, paint);
            // 绘制手指跟踪的圆形
            int endCircleX = endPoint.x;
            int endCircleY = endPoint.y;
            canvas.drawCircle(endCircleX, endCircleY, originRadius, paint);
            Rect textBounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, endCircleX, endCircleY + (textBounds.bottom - textBounds.top) / 2, textPaint);
            if (!isArrivedMaxMoved) { // 没有达到拉伸最大值
                path.reset();
                double sin = triangle.deltaY / triangle.hypotenuse;
                double cos = triangle.deltaX / triangle.hypotenuse;

                // A点
                path.moveTo(
                        (float) (startCircleX - curRadius * sin),
                        (float) (startCircleY - curRadius * cos)
                );
                // B点
                path.lineTo(
                        (float) (startCircleX + curRadius * sin),
                        (float) (startCircleY + curRadius * cos)
                );
                // C点
                path.quadTo(
                        (startCircleX + endCircleX) / 2, (startCircleY + endCircleY) / 2,
                        (float) (endCircleX + originRadius * sin), (float) (endCircleY + originRadius * cos)
                );
                // D点
                path.lineTo(
                        (float) (endCircleX - originRadius * sin),
                        (float) (endCircleY - originRadius * cos)
                );
                // A点
                path.quadTo(
                        (startCircleX + endCircleX) / 2, (startCircleY + endCircleY) / 2,
                        (float) (startCircleX - curRadius * sin), (float) (startCircleY - curRadius * cos)
                );
                canvas.drawPath(path, paint);
            }

        } else { // 非触摸状态
            if (curRadius > 0) {
                startCircleX = curRadius;
                startCircleY = originHeight - curRadius;
                canvas.drawCircle(startCircleX, startCircleY, curRadius, paint);
                Rect textBounds = new Rect();
                textPaint.getTextBounds(text, 0, text.length(), textBounds);
                canvas.drawText(text, startCircleX, startCircleY + (textBounds.bottom - textBounds.top) / 2, textPaint);
            } else if (curRadius < 0 && curRadius + 5 >= 0) {
                Bitmap unscaledBitmap = ScaleImage.decodeFile(context, exposeDrawable[curRadius + 5], originWidth, originHeight, ScaleImage.ScalingLogic.FIT);
                Bitmap scaledBitmap = ScaleImage.createScaledBitmap(unscaledBitmap, originWidth * 2, originHeight * 2, ScaleImage.ScalingLogic.FIT);
                canvas.drawBitmap(scaledBitmap, endPoint.x - scaledBitmap.getWidth() / 2, endPoint.y - scaledBitmap.getHeight() / 2, paint);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouched = true;
                this.setLayoutParams(newLp);
                changeViewWidthAndHeight(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                postInvalidate();
                endPoint.x = (int) downX;
                endPoint.y = (int) downY;
                downX = event.getX() + location[0];
                downY = event.getY() + location[1];
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算直角边和斜边（用于计算绘制两圆之间的填充去）
                triangle.deltaX = event.getX() - downX;
                triangle.deltaY = -1 * (event.getY() - downY); // y轴方向相反，所以需要取反
                triangle.getDistance();
                refreshCurRadiusByMoveDistance((int) triangle.hypotenuse);
                endPoint.x = (int) event.getX();
                endPoint.y = (int) event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouched = false;
                if (isArrivedMaxMoved) { // 触发消失动画
                    startExposeAnimation(animInterval);/*ms*/
                } else { // 触发还原动画
                    this.setLayoutParams(originLp);
                    changeViewWidthAndHeight(this, originWidth, originHeight);
                    if (null != onDraggableFlagViewListener) {
                        onDraggableFlagViewListener.onFlagUnDismiss(this);
                    }
                    startRollBackAnimation(animInterval);/*ms*/
                }
                downX = Float.MAX_VALUE;
                downY = Float.MAX_VALUE;
                break;
        }
        return true;
    }

    /**
     * 触发事件之后重置
     */

    private void resetAfterDismiss() {
        this.setVisibility(GONE);
        isArrivedMaxMoved = false;
        curRadius = originRadius;
        this.setLayoutParams(originLp);
        changeViewWidthAndHeight(this, originWidth, originHeight);
        postInvalidate();
    }

    /**
     * 根据移动的距离来刷新原来的圆半径大小
     *
     * @param distance
     */
    private void refreshCurRadiusByMoveDistance(int distance) {
        if (distance > maxMoveLength) {
            isArrivedMaxMoved = true;
            curRadius = 0;
        } else {
            isArrivedMaxMoved = false;
            float calcRadius = (1 - 1f * distance / maxMoveLength) * originRadius;
            float minRadius = ScreenUtil.dip2px(context, MIN_RADIUS);
            curRadius = (int) Math.max(calcRadius, minRadius);
        }
    }

    public void reset() {
        this.setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        startExposeAnimation(animInterval);
    }

    private void changeViewWidthAndHeight(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (null == lp) {
            lp = originLp;
        }
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    private int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    private void startRollBackAnimation(long duration) {
        if (null == rollBackAnim) {
            rollBackAnim = ValueAnimator.ofFloat(curRadius, originRadius);
            rollBackAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    curRadius = (int) value;
                    postInvalidate();
                }
            });
            rollBackAnim.setInterpolator(new BounceInterpolator()); // 反弹效果
            rollBackAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    curRadius = originRadius;
                    DraggableFlagView.this.clearAnimation();
                }
            });
        }
        rollBackAnim.setDuration(duration);
        rollBackAnim.start();
    }

    private void startExposeAnimation(long duration) {
        if (null == exposeAnim) {
            exposeAnim = ValueAnimator.ofFloat(-6, 0);//选出展示动画的drawable
            exposeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    curRadius = (int) value;
                    postInvalidate();
                }
            });
            exposeAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (null != onDraggableFlagViewListener) {
                        onDraggableFlagViewListener.onFlagDismiss(DraggableFlagView.this);
                    }
                    resetAfterDismiss();
                    DraggableFlagView.this.clearAnimation();
                }
            });
        }
        exposeAnim.setDuration(duration);
        exposeAnim.start();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public void setPaintColor(int color) {
        patientColor = color;
        paint.setColor(patientColor);
    }

    public void setTextColor(int color) {
        textColor = color;
        textPaint.setColor(textColor);
    }

    public void setTextSize(int size) {
        textSize = size;
        textPaint.setTextSize(ScreenUtil.sp2px(context, textSize));
    }

    public void setMaxMove(int maxMove) {
        maxMoveLength = maxMove;
    }

    public void setAnimInterval(int interval) {
        animInterval = interval;
    }

    public void setOnDraggableFlagViewListener(OnDraggableFlagViewListener onDraggableFlagViewListener) {
        this.onDraggableFlagViewListener = onDraggableFlagViewListener;
    }

    /**
     * 计算四个坐标的三角边关系
     */
    class Triangle {
        double deltaX;
        double deltaY;
        double hypotenuse;

        @Override
        public String toString() {
            return "Triangle{" + "deltaX=" + deltaX + ", deltaY=" + deltaY + ", hypotenuse=" + hypotenuse + '}';
        }

        public void getDistance() {
            hypotenuse = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        }
    }
}