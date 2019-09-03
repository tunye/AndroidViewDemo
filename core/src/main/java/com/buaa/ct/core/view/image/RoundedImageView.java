/*
	Copyright (C) 2013 Make Ramen, LLC
*/

package com.buaa.ct.core.view.image;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.buaa.ct.core.R;


public class RoundedImageView extends AppCompatImageView {

    public static final String TAG = "RoundedImageView";

    public static final int DEFAULT_RADIUS = 0;
    public static final int DEFAULT_BORDER = 0;
    public static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    public static final int DEFAULT_AVATARV_SIZE = Integer.MIN_VALUE;
    private static final ScaleType[] sScaleTypeArray = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };
    private int mCornerRadius;
    private int mBorderWidth;
    private int mBorderColor;
    private int mCoverBorderWidth;
    private int mCoverBorderColor;
    private boolean roundBackground;
    private Drawable mDrawable;
    private Drawable mBackgroundDrawable;
    private ScaleType mScaleType;
    private boolean isEnableRounded = true;
    private Drawable mForeGroundDrawable;
    private Drawable mCenterDrawable;
    private boolean avatarVBorder = false;
    private int avatarVSize = DEFAULT_AVATARV_SIZE;
    private int avatarDis = 0;
    private Paint avatarvbgPaint;
    private RectF avatarvbgRect;

    public RoundedImageView(Context context) {
        super(context);
        mCornerRadius = DEFAULT_RADIUS;
        mBorderWidth = DEFAULT_BORDER;
        mBorderColor = DEFAULT_BORDER_COLOR;
        mCoverBorderWidth = DEFAULT_BORDER;
        mCoverBorderColor = DEFAULT_BORDER_COLOR;
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

//	@Override
//	public void setBackground(Drawable background) {
//		setBackgroundDrawable(background);
//	}

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

        int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        }

        mCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_corner_radius, -1);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_border_width, -1);
        mCoverBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_cover_border_width, -1);
        // don't allow negative values for radius and border
        if (mCornerRadius < 0) {
            mCornerRadius = DEFAULT_RADIUS;
        }
        if (mBorderWidth < 0) {
            mBorderWidth = DEFAULT_BORDER;
        }
        if (mCoverBorderWidth < 0) {
            mCoverBorderWidth = DEFAULT_BORDER;
        }
        if (avatarVSize < 0) {
            avatarVSize = DEFAULT_AVATARV_SIZE;
        }

        mBorderColor = a.getColor(R.styleable.RoundedImageView_border_color, DEFAULT_BORDER_COLOR);
        mCoverBorderColor = a.getColor(R.styleable.RoundedImageView_cover_border_color, DEFAULT_BORDER_COLOR);

        roundBackground = a.getBoolean(R.styleable.RoundedImageView_round_background, false);

        a.recycle();
        init();
    }

    private void init() {
        avatarvbgPaint = new Paint();
        avatarvbgPaint.setColor(Color.WHITE);
        avatarvbgPaint.setAntiAlias(true);

        avatarvbgRect = new RectF();
    }

    /**
     * Return the current scale type in use by this ImageView.
     *
     * @attr ref android.R.styleable#ImageView_scaleType
     * @see ImageView.ScaleType
     */
    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * Controls how the image should be resized or moved to match the size
     * of this ImageView.
     *
     * @param scaleType The desired scaling mode.
     * @attr ref android.R.styleable#ImageView_scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            if (mDrawable instanceof RoundedDrawable
                    && ((RoundedDrawable) mDrawable).getScaleType() != scaleType) {
                ((RoundedDrawable) mDrawable).setScaleType(scaleType);
            }

            if (mBackgroundDrawable instanceof RoundedDrawable
                    && ((RoundedDrawable) mBackgroundDrawable).getScaleType() != scaleType) {
                ((RoundedDrawable) mBackgroundDrawable).setScaleType(scaleType);
            }
            setWillNotCacheDrawing(true);
            requestLayout();
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (isEnableRounded && drawable != null) {
            mDrawable = RoundedDrawable.fromDrawable(drawable, mCornerRadius, mBorderWidth, mBorderColor, mCoverBorderWidth, mCoverBorderColor);
            if (mDrawable instanceof RoundedDrawable) {
                ((RoundedDrawable) mDrawable).setScaleType(mScaleType);
                ((RoundedDrawable) mDrawable).setCornerRadius(mCornerRadius);
                ((RoundedDrawable) mDrawable).setBorderWidth(mBorderWidth);
                ((RoundedDrawable) mDrawable).setBorderColor(mBorderColor);
                ((RoundedDrawable) mDrawable).setCoverBorderWidth(mCoverBorderWidth);
                ((RoundedDrawable) mDrawable).setCoverBorderColor(mCoverBorderColor);
            }
        } else {
            mDrawable = drawable;
        }
        super.setImageDrawable(mDrawable);
    }

    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            mDrawable = new RoundedDrawable(bm, mCornerRadius, mBorderWidth, mBorderColor, mCoverBorderWidth, mCoverBorderColor);
            if (mDrawable instanceof RoundedDrawable) {
                ((RoundedDrawable) mDrawable).setScaleType(mScaleType);
                ((RoundedDrawable) mDrawable).setCornerRadius(mCornerRadius);
                ((RoundedDrawable) mDrawable).setBorderWidth(mBorderWidth);
                ((RoundedDrawable) mDrawable).setBorderColor(mBorderColor);
                ((RoundedDrawable) mDrawable).setCoverBorderWidth(mCoverBorderWidth);
                ((RoundedDrawable) mDrawable).setCoverBorderColor(mCoverBorderColor);
            }
        } else {
            mDrawable = null;
        }
        super.setImageDrawable(mDrawable);
    }

    public void setBackbgWithOutRund(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mForeGroundDrawable != null) {
            mForeGroundDrawable.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * 设置前景 Drawable
     *
     * @param foreGroundDrawable
     */
    public void setForeGroundDrawable(Drawable foreGroundDrawable) {
        if (mForeGroundDrawable != null) {
            mForeGroundDrawable.setCallback(null);
        }
        this.mForeGroundDrawable = foreGroundDrawable;
        if (mForeGroundDrawable != null) {
            mForeGroundDrawable.setCallback(this);
        }
    }

    /**
     * 设置位于最顶部中心的Drawable
     *
     * @param centerDrawable
     */
    public void setTopCenterDrawable(Drawable centerDrawable) {
        if (mCenterDrawable != null) {
            mCenterDrawable.setCallback(null);
        }
        this.mCenterDrawable = centerDrawable;
        if (mCenterDrawable != null) {
            mCenterDrawable.setCallback(this);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mForeGroundDrawable != null && mForeGroundDrawable.isStateful()) {
            mForeGroundDrawable.setState(getDrawableState());
        }

        if (mCenterDrawable != null && mCenterDrawable.isStateful()) {
            mCenterDrawable.setState(getDrawableState());
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        if (mForeGroundDrawable != null && mForeGroundDrawable == dr) {
            return true;
        }
        if (mCenterDrawable != null && mCenterDrawable == dr) {
            return true;
        }
        return super.verifyDrawable(dr);
    }

    @Override
    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        if (roundBackground && background != null) {
            mBackgroundDrawable = RoundedDrawable.fromDrawable(background, mCornerRadius, mBorderWidth, mBorderColor, mCoverBorderWidth, mCoverBorderColor);
            if (mBackgroundDrawable instanceof RoundedDrawable) {
                ((RoundedDrawable) mBackgroundDrawable).setScaleType(mScaleType);
                ((RoundedDrawable) mBackgroundDrawable).setCornerRadius(mCornerRadius);
                ((RoundedDrawable) mBackgroundDrawable).setBorderWidth(mBorderWidth);
                ((RoundedDrawable) mBackgroundDrawable).setBorderColor(mBorderColor);
                ((RoundedDrawable) mBackgroundDrawable).setCoverBorderWidth(mCoverBorderWidth);
                ((RoundedDrawable) mBackgroundDrawable).setCoverBorderColor(mCoverBorderColor);
            }
        } else {
            mBackgroundDrawable = background;
        }
        super.setBackgroundDrawable(mBackgroundDrawable);
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(int radius) {
        if (mCornerRadius == radius) {
            return;
        }

        this.mCornerRadius = radius;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setCornerRadius(radius);
        }
        if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setCornerRadius(radius);
        }
    }

    public int getBorder() {
        return mBorderWidth;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int color) {
        if (mBorderColor == color) {
            return;
        }

        this.mBorderColor = color;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setBorderColor(color);
        }
        if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setBorderColor(color);
        }
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    public int getCoverBorder() {
        return mCoverBorderWidth;
    }

    public int getCoverBorderColor() {
        return mCoverBorderColor;
    }

    public void setCoverBorderColor(int color) {
        if (mCoverBorderColor == color) {
            return;
        }

        this.mCoverBorderColor = color;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setCoverBorderColor(color);
        }
        if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setCoverBorderColor(color);
        }
        if (mCoverBorderWidth > 0) {
            invalidate();
        }
    }

    public void setBorderWidth(int width) {
        if (mBorderWidth == width) {
            return;
        }

        this.mBorderWidth = width;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setBorderWidth(width);
        }
        if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setBorderWidth(width);
        }
        invalidate();
    }

    public void setCoverBorderWidth(int width) {
        if (mCoverBorderWidth == width) {
            return;
        }

        this.mCoverBorderWidth = width;
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setCoverBorderWidth(width);
        }
        if (roundBackground && mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setCoverBorderWidth(width);
        }
        invalidate();
    }

    public boolean isRoundBackground() {
        return roundBackground;
    }

    public void setRoundBackground(boolean roundBackground) {
        if (this.roundBackground == roundBackground) {
            return;
        }

        this.roundBackground = roundBackground;
        if (roundBackground) {
            if (mBackgroundDrawable instanceof RoundedDrawable) {
                ((RoundedDrawable) mBackgroundDrawable).setScaleType(mScaleType);
                ((RoundedDrawable) mBackgroundDrawable).setCornerRadius(mCornerRadius);
                ((RoundedDrawable) mBackgroundDrawable).setBorderWidth(mBorderWidth);
                ((RoundedDrawable) mBackgroundDrawable).setBorderColor(mBorderColor);
                ((RoundedDrawable) mBackgroundDrawable).setCoverBorderWidth(mCoverBorderWidth);
                ((RoundedDrawable) mBackgroundDrawable).setCoverBorderColor(mCoverBorderColor);
            } else {
                setBackgroundDrawable(mBackgroundDrawable);
            }
        } else if (mBackgroundDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mBackgroundDrawable).setBorderWidth(0);
            ((RoundedDrawable) mBackgroundDrawable).setBorderColor(0);
            ((RoundedDrawable) mBackgroundDrawable).setCornerRadius(0);
            ((RoundedDrawable) mBackgroundDrawable).setCoverBorderWidth(0);
            ((RoundedDrawable) mBackgroundDrawable).setCoverBorderColor(0);
        }

        invalidate();
    }

    public boolean isEnableRounded() {
        return isEnableRounded;
    }

    public void setEnableRounded(boolean isEnableRounded) {
        this.isEnableRounded = isEnableRounded;
    }

    public void setPortraitAvatarVBorder(boolean border) {
        avatarVBorder = border;
    }

    public void setAvatarDistance(int dis) {
        this.avatarDis = dis;
    }

    /**
     * size 为px
     *
     * @param size
     */
    public void setAvatarVSize(int size) {
        this.avatarVSize = size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable;
        if (mForeGroundDrawable != null) {
            mForeGroundDrawable.setBounds(0, 0, getWidth(), getHeight());
            mForeGroundDrawable.draw(canvas);
        }

        if (mCenterDrawable != null) {
            canvas.save();
            canvas.translate(getWidth() / 2 - mCenterDrawable.getIntrinsicWidth() / 2, getHeight() / 2 - mCenterDrawable.getIntrinsicHeight() / 2);
            mCenterDrawable.setBounds(0, 0, mCenterDrawable.getIntrinsicWidth(), mCenterDrawable.getIntrinsicHeight());
            mCenterDrawable.draw(canvas);
            canvas.restore();
        }
    }
}
