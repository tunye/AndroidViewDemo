package com.buaa.ct.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;

import com.buaa.ct.core.manager.RuntimeManager;


/**
 * Created by 10202 on 2015/10/10.
 */
public class MyPalette {
    private Palette palette;

    private int darkVibrantColor;//暗鲜艳色
    private int darkMutedColor;//暗柔和的颜色
    private int lightVibrantColor;//亮鲜艳色(淡色)
    private int lightMutedColor;//亮柔和色(淡色)
    private int mutedColor;//柔和色
    private int vibrantColor;//鲜艳色

    public void getByResource(int drawableID, final PaletteFinish paletteFinish) {
        Bitmap bm = BitmapFactory.decodeResource(RuntimeManager.getInstance().getContext().getResources(), drawableID);
        Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette generatePalette) {
                palette = generatePalette;
                generateAllColor();
                paletteFinish.finish();
            }
        });
    }

    public void getByDrawable(Drawable drawable, final PaletteFinish paletteFinish) {
        Bitmap bm = drawableToBitmap(drawable);
        Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette generatePalette) {
                palette = generatePalette;
                generateAllColor();
                paletteFinish.finish();
            }
        });
    }

    public void getByBitmap(Bitmap bm, final PaletteFinish paletteFinish) {
        Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette generatePalette) {
                palette = generatePalette;
                generateAllColor();
                paletteFinish.finish();
            }
        });
    }

    private void generateAllColor() {
        darkVibrantColor = palette.getDarkVibrantColor(0);
        darkMutedColor = palette.getDarkMutedColor(0);
        lightVibrantColor = palette.getLightVibrantColor(0);
        lightMutedColor = palette.getLightMutedColor(0);
        mutedColor = palette.getMutedColor(0);
        vibrantColor = palette.getVibrantColor(0);
    }

    public Palette getPalette() {
        return palette;
    }

    public int getDarkVibrantColor() {
        return darkVibrantColor;
    }

    public int getDarkMutedColor() {
        return darkMutedColor;
    }

    public int getLightVibrantColor() {
        return lightVibrantColor;
    }

    public int getLightMutedColor() {
        return lightMutedColor;
    }

    public int getMutedColor() {
        return mutedColor;
    }

    public int getVibrantColor() {
        return vibrantColor;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public interface PaletteFinish {
        void finish();
    }
}
