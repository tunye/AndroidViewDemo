package com.buaa.ct.imageselector.view;

import android.os.Bundle;
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
import com.buaa.ct.core.util.ImageUtil;
import com.buaa.ct.imageselector.R;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;


/**
 * Created by dee on 15/11/25.
 */
public class ImagePreviewFragment extends Fragment {
    public static final String PATH = "path";

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
        final PhotoView photoView = contentView.findViewById(R.id.preview_image);
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
                    CoreBaseActivity activity = (CoreBaseActivity) getActivity();
                    if (activity instanceof ImagePreviewActivity) {
                        ((ImagePreviewActivity) activity).switchBarVisibility();
                    } else if (activity instanceof OnlyPreviewActivity) {
                        ((OnlyPreviewActivity) activity).switchBarVisibility();
                    }
                }
            });
        }
        return contentView;
    }
}
