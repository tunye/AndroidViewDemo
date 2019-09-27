package com.buaa.ct.imageselector.view;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.ImageUtil;
import com.buaa.ct.core.util.SpringUtil;
import com.buaa.ct.core.util.ThreadPoolUtil;
import com.buaa.ct.core.util.ThreadUtils;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.photo.PhotoView;
import com.buaa.ct.imageselector.photo.listener.OnPhotoTapListener;
import com.buaa.ct.imageselector.photo.listener.OnSingleFingerMoveListener;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by dee on 15/11/25.
 */
public class ImagePreviewFragment extends Fragment {
    public static final String PATH = "path";
    private PhotoView photoView;
    private boolean enableCloseListener;
    public static ImagePreviewFragment getInstance(String path) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_image_preview, container, false);
        photoView = contentView.findViewById(R.id.preview_image);
        photoView.setImageAlpha(255);
        if (getArguments() == null) {
            return container;
        } else {
            String imageUrl = getArguments().getString(PATH);
            if (TextUtils.isEmpty(imageUrl)) {
                return container;
            }
            try {
                ImageUtil.loadImage(imageUrl, photoView);
            } catch (Throwable t) {
                // 可能oom
                if (ImageUtil.isLocalPic(imageUrl)) {
                    Pair<Integer, Integer> widthAndHeight = ImageUtil.getImageFileRealWidthAndHeight(imageUrl);
                    ImageUtil.loadImage(imageUrl, photoView, new RequestOptions().override(widthAndHeight.first / 2, widthAndHeight.second / 2));
                }
            }
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    hideToolBar();
                }
            });
            photoView.setOnSingleFingerMoveListener(new OnSingleFingerMoveListener() {
                @Override
                public void move(float diffX, float diffY) {
                    float ratio = diffY / RuntimeManager.getInstance().getScreenHeight() * 2;
                    if (Math.abs(diffY) > 2 * Math.abs(diffX) && diffY > 0) {
                        enableCloseListener = true;
                    }
                    if (enableCloseListener) {
                        scale(diffX, diffY, ratio);
                    }
                }

                @Override
                public void release(final float diffX, final float diffY, boolean cancel) {
                    float ratio = diffY / RuntimeManager.getInstance().getScreenHeight() * 2;
                    if (enableCloseListener)
                        if (cancel || (diffY > 0 && ratio < 0.2)) {
                            SpringUtil.getInstance().addListener(new SimpleSpringListener() {
                                @Override
                                public void onSpringUpdate(Spring spring) {
                                    super.onSpringUpdate(spring);
                                    float ratio = (float) spring.getCurrentValue();
                                    scale(diffX * ratio, diffY * ratio, ratio);
                                }

                                @Override
                                public void onSpringAtRest(Spring spring) {
                                    super.onSpringAtRest(spring);
                                    SpringUtil.getInstance().destory();
                                }
                            });
                            SpringUtil.getInstance().setCurrentValue(ratio);
                            SpringUtil.getInstance().setEndValue(0);
                        } else if (diffY > 0) {
                            SpringUtil.getInstance().addListener(new SimpleSpringListener() {
                                @Override
                                public void onSpringUpdate(Spring spring) {
                                    super.onSpringUpdate(spring);
                                    float ratio = (float) spring.getCurrentValue();
                                    scale(diffX * ratio, diffY * ratio, ratio);
                                }

                                @Override
                                public void onSpringAtRest(Spring spring) {
                                    super.onSpringAtRest(spring);
                                    SpringUtil.getInstance().destory();
                                    getActivity().finish();
                                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out_fast);
                                }
                            });
                            SpringUtil.getInstance().setCurrentValue(ratio);
                            SpringUtil.getInstance().setEndValue(1);
                        }

                    enableCloseListener = false;
                }
            });
        }
        return contentView;
    }

    private void scale(final float diffX, final float diffY, float ratio) {
        CoreBaseActivity activity = (CoreBaseActivity) getActivity();
        if (activity instanceof ImagePreviewActivity) {
            // 选图界面不建议增加下滑返回
//            ((ImagePreviewActivity) activity).scale(diffX, diffY, 1 - ratio);
        } else if (activity instanceof OnlyPreviewActivity) {
            ((OnlyPreviewActivity) activity).scale(diffX, diffY, 1 - ratio);
            photoView.setImageAlpha((int) ((1 - ratio) * 255));
        }
    }

    private void hideToolBar() {
        CoreBaseActivity activity = (CoreBaseActivity) getActivity();
        if (activity instanceof ImagePreviewActivity) {
            ((ImagePreviewActivity) activity).switchBarVisibility();
        } else if (activity instanceof OnlyPreviewActivity) {
            ((OnlyPreviewActivity) activity).switchBarVisibility();
        }
    }

    public void save() {
        Bundle param = getArguments();
        if (param == null) {
            return;
        }
        final String imageUrl = param.getString(PATH);
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                ImageUtil.downLoadImage(imageUrl, RuntimeManager.getInstance().getContext(), null, new ImageUtil.OnBitmapLoaded() {
                    @Override
                    public void onImageLoaded(Bitmap bitmap) {
                        try {
                            final String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()
                                    + File.separator + "Camera" + File.separator + System.currentTimeMillis() + ".jpg";
                            File saveFile = new File(savePath);
                            FileOutputStream out = new FileOutputStream(saveFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                            insertPicToDB(RuntimeManager.getInstance().getContext(), RuntimeManager.getInstance().getString(R.string.app_name), 0, saveFile);
                            ThreadUtils.postOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomToast.getInstance().showToast(RuntimeManager.getInstance().getString(R.string.save_success) + savePath);
                                }
                            });
                        } catch (IOException e) {
                            // do nothing
                        }
                    }

                    @Override
                    public void onImageLoadFailed() {

                    }
                });
            }
        });
    }

    public static void insertPicToDB(Context context, String picDescription, int orientation, File file) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis());

        ContentValues v = new ContentValues();

        v.put(MediaStore.MediaColumns.TITLE, file.getAbsolutePath());
        v.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getAbsolutePath());
        v.put(MediaStore.Images.ImageColumns.DESCRIPTION, picDescription);
        v.put(MediaStore.MediaColumns.DATE_ADDED, calendar.getTimeInMillis());
        v.put(MediaStore.Images.ImageColumns.DATE_TAKEN, calendar.getTimeInMillis());
        v.put(MediaStore.MediaColumns.DATE_MODIFIED, calendar.getTimeInMillis());
        v.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        v.put(MediaStore.Images.ImageColumns.ORIENTATION, orientation);
        v.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());

        File parent = file.getParentFile();
        String path = parent.toString().toLowerCase(Locale.getDefault());
        String name = parent.getName().toLowerCase(Locale.getDefault());
        v.put(MediaStore.Images.ImageColumns.BUCKET_ID, path.hashCode());
        v.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, name);

        ContentResolver c = context.getContentResolver();
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = {file.getAbsolutePath()};
        int result = c.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v, selection, selectionArgs);
        if (result == 0) {
            c.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);
        }

        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        RuntimeManager.getInstance().getContext().sendBroadcast(intent);
    }
}
