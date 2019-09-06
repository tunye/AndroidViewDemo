package com.buaa.ct.imageselector.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.util.ThreadUtils;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.utils.CropUtil;
import com.buaa.ct.imageselector.utils.FileUtils;
import com.buaa.ct.imageselector.widget.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dee on 15/11/26.
 */
public class ImageCropActivity extends CoreBaseActivity {
    public static final String EXTRA_PATH = "extraPath";
    public static final String OUTPUT_PATH = "outputPath";
    public static final int REQUEST_CROP = 69;
    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;

    private CropImageView cropImageView;
    private Uri sourceUri;
    private Uri saveUri;

    public static void startCrop(Activity activity, String path) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        activity.startActivityForResult(intent, REQUEST_CROP);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_crop;
    }

    @Override
    public void beforeSetLayout(Bundle saveBundle) {
        super.beforeSetLayout(saveBundle);
        String path = getIntent().getStringExtra(EXTRA_PATH);
        File f = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // android N获取uri的新方式
            sourceUri = FileProvider.getUriForFile(this, getApplication().getPackageName(), f);
        } else {
            sourceUri = Uri.fromFile(f);
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        cropImageView = findViewById(R.id.cropImageView);
    }

    @Override
    public void setListener() {
        super.setListener();
        toolbarOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog.show(
                        ImageCropActivity.this, null, getString(R.string.save_ing), true, false);

                saveUri = Uri.fromFile(FileUtils.createCropFile(ImageCropActivity.this));
                saveOutput(cropImageView.getCroppedBitmap());
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        cropImageView.setHandleSizeInDp(10);
        toolbarOper.setText(R.string.use);
        int exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri));

        InputStream is = null;
        try {
            int sampleSize = calculateBitmapSampleSize(sourceUri);
            is = getContentResolver().openInputStream(sourceUri);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = sampleSize;
            Bitmap sizeBitmap = BitmapFactory.decodeStream(is, null, option);
            Matrix matrix = getRotateMatrix(sizeBitmap, exifRotation % 360);
            Bitmap rotated = Bitmap.createBitmap(sizeBitmap, 0, 0, sizeBitmap.getWidth(), sizeBitmap.getHeight(), matrix, true);
            cropImageView.setImageBitmap(rotated);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CropUtil.closeSilently(is);
        }
    }

    public Matrix getRotateMatrix(Bitmap bitmap, int rotation) {
        Matrix matrix = new Matrix();
        if (bitmap != null && rotation != 0) {
            int cx = bitmap.getWidth() / 2;
            int cy = bitmap.getHeight() / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(rotation);
            matrix.postTranslate(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        }
        return matrix;
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        } finally {
            CropUtil.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CropUtil.closeSilently(outputStream);
            }
            setResult(RESULT_OK, new Intent().putExtra(OUTPUT_PATH, saveUri.getPath()));
        }
        final Bitmap b = croppedImage;
        ThreadUtils.postOnUiThread(new Runnable() {
            public void run() {
                b.recycle();
            }
        });
        finish();
    }
}
