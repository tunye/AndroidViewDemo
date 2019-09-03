package com.buaa.ct.core.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.buaa.ct.core.manager.RuntimeManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by 102 on 2016/10/10.
 */

public class ImageUtil {
    public static void loadImage(String imageUrl, ImageView imageView) {
        loadImage(imageUrl, imageView, null);
    }

    public static void loadImage(String imageUrl, ImageView imageView, RequestOptions requestOptions) {
        loadImage(imageUrl, imageView, requestOptions, null);
    }

    public static void loadImage(String imageUrl, ImageView imageView, RequestOptions requestOptions, final OnDrawableLoadListener listener) {
        if (imageView == null) {
            return;
        } else if (TextUtils.isEmpty(imageUrl) && requestOptions != null) {
            imageView.setImageResource(requestOptions.getErrorId());
            return;
        }
        Context context;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            context = imageView.getContext();
        } else {
            context = RuntimeManager.getInstance().getContext();
        }
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(imageUrl);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }

        requestBuilder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (listener != null) {
                    listener.onFail(e);
                }
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (listener != null) {
                    listener.onSuccess(resource);
                }
                return true;
            }
        }).into(imageView);
    }

    public static void preLoadImage(String imageUrl, ImageView imageView, RequestOptions requestOptions, final OnDrawableLoadListener listener) {
        if (imageView == null) {
            return;
        } else if (TextUtils.isEmpty(imageUrl) && requestOptions != null) {
            imageView.setImageResource(requestOptions.getErrorId());
            return;
        }
        Context context;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            context = imageView.getContext();
        } else {
            context = RuntimeManager.getInstance().getContext();
        }
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(imageUrl);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }

        requestBuilder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (listener != null) {
                    listener.onFail(e);
                }
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (listener != null) {
                    listener.onSuccess(resource);
                }
                return true;
            }
        }).preload();
    }

    public static void downLoadImage(String imageUrl, ImageView imageView, RequestOptions requestOptions, final OnDrawableLoadListener listener) {
        if (imageView == null) {
            return;
        } else if (TextUtils.isEmpty(imageUrl) && requestOptions != null) {
            imageView.setImageResource(requestOptions.getErrorId());
            return;
        }
        Context context;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            context = imageView.getContext();
        } else {
            context = RuntimeManager.getInstance().getContext();
        }
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().load(imageUrl);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }

        requestBuilder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (listener != null) {
                    listener.onFail(e);
                }
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (listener != null) {
                    listener.onSuccess(resource);
                }
                return true;
            }
        }).submit();
    }


    /**
     * 清空内存缓存
     * 内存缓存必须得在主线程
     *
     * @param activity Activity的Context
     */
    public static void clearMemoryCache(final Context activity) {
        Glide.get(activity).clearMemory();
    }

    /**
     * 清空磁盘缓存
     * 必须得在子线程运行
     *
     * @param context context
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    /**
     * 双清空缓存
     */
    public static void clearImageAllCache(Context context) {
        clearDiskCache(context);
        clearMemoryCache(context);
    }

    public static void destroy(Application application) {
        clearMemoryCache(application);
        Glide.with(application).onDestroy();
    }

    public RequestOptions getRequestOptions(int placeholderDrawableId) {
        return getRequestOptions(placeholderDrawableId, 5);
    }

    public RequestOptions getRequestOptions(int placeholderDrawableId, int roundCorners) {
        return getRequestOptions(placeholderDrawableId, roundCorners, DiskCacheStrategy.RESOURCE);
    }

    public RequestOptions getRequestOptions(int placeholderDrawableId, int roundCorners, DiskCacheStrategy strategy) {
        return getRequestOptions(placeholderDrawableId, roundCorners, strategy, true);
    }

    public RequestOptions getRequestOptions(int placeholderDrawableId, int roundCorners, DiskCacheStrategy strategy, boolean centerCrop) {
        RequestOptions requestOptions;
        if (roundCorners == 0) {
            requestOptions = new RequestOptions();
        } else {
            requestOptions = RequestOptions.bitmapTransform(new RoundedCorners(roundCorners));
        }
        requestOptions = requestOptions.diskCacheStrategy(strategy);
        if (centerCrop) {
            requestOptions = requestOptions.centerCrop();
        } else {
            requestOptions = requestOptions.fitCenter();
        }
        requestOptions = requestOptions.placeholder(placeholderDrawableId);
        requestOptions = requestOptions.error(placeholderDrawableId);
        return requestOptions;
    }

    public interface OnDrawableLoadListener {
        void onSuccess(Bitmap bitmap);

        void onFail(Exception e);
    }
}
