/*
	Copyright (C) 2013 Make Ramen, LLC
*/

package com.buaa.ct.core.view.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;

public class RoundedDrawable extends Drawable {
    public static final String TAG = "RoundedDrawable";

    private final RectF mBounds = new RectF();

    private final RectF mDrawableRect = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final RectF mBorderRect = new RectF();
    private final Paint mBorderPaint;
    private final Paint mCoverBorderPaint;
    private final Matrix mShaderMatrix = new Matrix();
    private float mCornerRadius;
    private int mBorderWidth;
    private int mBorderColor;
    private int mCoverBorderWidth;
    private int mCoverBorderColor;
    private ScaleType mScaleType = ScaleType.FIT_XY;

    RoundedDrawable(Bitmap bitmap, float cornerRadius, int border, int borderColor, int coverBorder, int coverBorderColor) {

        mBorderWidth = border;
        mBorderColor = borderColor;

        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mCoverBorderWidth = coverBorder;
        mCoverBorderColor = coverBorderColor;

        mCornerRadius = cornerRadius;
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(border);
        mBitmapPaint.setShader(mBitmapShader);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setDither(true);

        mCoverBorderPaint = new Paint();
        mCoverBorderPaint.setAntiAlias(true);
        mCoverBorderPaint.setColor(mCoverBorderColor);
        mCoverBorderPaint.setStrokeWidth(mCoverBorderWidth);
        mCoverBorderPaint.setStyle(Style.STROKE);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            bitmap = null;
        }

        return bitmap;
    }

    public static Drawable fromDrawable(Drawable drawable, float radius) {
        return fromDrawable(drawable, radius, 0, 0, 0, 0);
    }

    public static Drawable fromDrawable(Drawable drawable, float radius, int border, int borderColor, int coverBorder, int coverBorderColor) {
        if (drawable != null) {
            if (drawable instanceof TransitionDrawable) {
                TransitionDrawable td = (TransitionDrawable) drawable;
                int num = td.getNumberOfLayers();

                Drawable[] drawableList = new Drawable[num];
                for (int i = 0; i < num; i++) {
                    Drawable d = td.getDrawable(i);
                    if (d instanceof ColorDrawable) {
                        // TODO skip colordrawables for now
                        drawableList[i] = d;
                    } else {
                        Bitmap bm = drawableToBitmap(drawable);
                        if (bm != null) {
                            drawableList[i] = new RoundedDrawable(bm, radius, border, borderColor, coverBorder, coverBorderColor);
                        }
                    }
                }
                return new TransitionDrawable(drawableList);
//		    }else if(drawable instanceof StateListDrawable){
//		    	//此处需要特殊处理
//		    	StateListDrawable sld = (StateListDrawable)drawable;
//		    	//int getStateCount()
//		    	//int[] getStateSet(int index)
//		    	//Drawable getStateDrawable(int index)
//		    	try {
//					Method getStateCountMethod = StateListDrawable.class.getMethod("getStateCount");
//					int count = (Integer) getStateCountMethod.invoke(sld);
//					Method getStateSetMethod = StateListDrawable.class.getMethod("getStateSet", int.class);
//					Method getStateDrawableMethod = StateListDrawable.class.getMethod("getStateDrawable", int.class);
//
//					StateListDrawable roundedsld = new StateListDrawable();
//					for(int i = 0; i < count; i++){
//						int[] states = (int[]) getStateSetMethod.invoke(sld, i);
//						Drawable d = (Drawable) getStateDrawableMethod.invoke(sld, i);
//						Bitmap b;
//						if(d instanceof ColorDrawable){
//							b = Bitmap.createBitmap((int)radius * 2, (int)radius * 2, Config.ARGB_8888);
//						}else{
//							b = drawableToBitmap(d);
//						}
//						d = new RoundedDrawable(b, radius, border, borderColor, coverBorder, coverBorderColor);
//						roundedsld.addState(states, d);
//					}
//					return roundedsld;
//				} catch (Exception e) {//NoSuchMethodException、IllegalArgumentException、IllegalAccessException、InvocationTargetException
//					drawable = sld.getCurrent();
//					Bitmap b;
//					if(drawable instanceof ColorDrawable){
//						b = Bitmap.createBitmap((int)radius * 2, (int)radius * 2, Config.ARGB_8888);
//					}else{
//						b = drawableToBitmap(drawable);
//					}
//					return new RoundedDrawable(b, radius, border, borderColor, coverBorder, coverBorderColor);
//				}
//
            }

            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundedDrawable(bm, radius, border, borderColor, coverBorder, coverBorderColor);
            } else {
                Log.w(TAG, "Failed to create bitmap from drawable!");
            }
        }
        return drawable;
    }

    protected ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_XY;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            setMatrix();
        }
    }

    private void setMatrix() {
        mBorderRect.set(mBounds);
        mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

        float scale;
        float dx;
        float dy;

        switch (mScaleType) {
            case CENTER:
//			Log.d(TAG, "CENTER");
                mBorderRect.set(mBounds);
                mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

                mShaderMatrix.set(null);
                mShaderMatrix.setTranslate((int) ((mDrawableRect.width() - mBitmapWidth) * 0.5f + 0.5f), (int) ((mDrawableRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;
            case CENTER_CROP:
//			Log.d(TAG, "CENTER_CROP");
                mBorderRect.set(mBounds);
                mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

                mShaderMatrix.set(null);

                dx = 0;
                dy = 0;

                if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
                    scale = (float) mDrawableRect.height() / (float) mBitmapHeight;
                    dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = (float) mDrawableRect.width() / (float) mBitmapWidth;
                    dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
                break;
            case CENTER_INSIDE:
//			Log.d(TAG, "CENTER_INSIDE");
                mShaderMatrix.set(null);

                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) mBounds.width() / (float) mBitmapWidth,
                            (float) mBounds.height() / (float) mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

                mBorderRect.set(mBitmapRect);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_CENTER:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_END:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_START:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth, mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
            case FIT_XY:
            default:
//			Log.d(TAG, "DEFAULT TO FILL");
                mBorderRect.set(mBounds);
                mDrawableRect.set(0 + mBorderWidth, 0 + mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
                mShaderMatrix.set(null);
                mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
                break;
        }
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
//		Log.i(TAG, "onboundschange: w: " + bounds.width() + "h:" + bounds.height());
        super.onBoundsChange(bounds);

        mBounds.set(bounds);

//		if (USE_VIGNETTE) {
//			RadialGradient vignette = new RadialGradient(
//					mDrawableRect.centerX(), mDrawableRect.centerY() * 1.0f / 0.7f, mDrawableRect.centerX() * 1.3f,
//					new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
//					Shader.TileMode.CLAMP);
//
//			Matrix oval = new Matrix();
//			oval.setScale(1.0f, 0.7f);
//			vignette.setLocalMatrix(oval);
//
//			mBitmapPaint.setShader(
//					new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
//		}

        setMatrix();
    }

    @Override
    public void draw(Canvas canvas) {
//		Log.w(TAG, "Draw: " + mScaleType.toString());
        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
            canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius - mBorderWidth, 0), Math.max(mCornerRadius - mBorderWidth, 0), mBitmapPaint);
            if (mCoverBorderWidth > 0) {
                canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius - mBorderWidth, 0), Math.max(mCornerRadius - mBorderWidth, 0), mCoverBorderPaint);
            }
        } else {
            canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
            if (mCoverBorderWidth > 0) {
                canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mCoverBorderPaint);
            }
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int width) {
        this.mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int color) {
        this.mBorderColor = color;
        mBorderPaint.setColor(color);
    }

    public void setCoverBorderWidth(int width) {
        this.mCoverBorderWidth = width;
        mCoverBorderPaint.setStrokeWidth(mCoverBorderWidth);
    }

    public void setCoverBorderColor(int color) {
        this.mCoverBorderColor = color;
        mCoverBorderPaint.setColor(color);
    }
}