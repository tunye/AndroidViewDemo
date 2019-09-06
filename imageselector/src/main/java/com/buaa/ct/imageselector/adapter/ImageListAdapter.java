package com.buaa.ct.imageselector.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.util.ImageUtil;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.model.LocalMedia;
import com.buaa.ct.imageselector.view.ImageSelectorActivity;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 */
public class ImageListAdapter extends CoreRecyclerViewAdapter<LocalMedia, CoreRecyclerViewAdapter.MyViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;

    private boolean showCamera;
    private boolean enablePreview;
    private int maxSelectNum;
    private int selectMode;

    private List<LocalMedia> selectImages = new ArrayList<>();

    private OnImageSelectChangedListener imageSelectChangedListener;

    public ImageListAdapter(Context context, int maxSelectNum, int mode, boolean showCamera, boolean enablePreview) {
        super(context);
        this.selectMode = mode;
        this.maxSelectNum = maxSelectNum;
        this.showCamera = showCamera;
        this.enablePreview = enablePreview;
    }

    public void setSource(List<LocalMedia> images) {
        addDatas(images);
    }

    public void bindSelectImages(List<LocalMedia> images) {
        this.selectImages = images;
        notifyDataSetChanged();
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    @NonNull
    @Override
    public CoreRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final CoreRecyclerViewAdapter.MyViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageSelectChangedListener != null) {
                        imageSelectChangedListener.onTakePhoto();
                    }
                }
            });
        } else {
            final ViewHolder contentHolder = (ViewHolder) holder;
            final LocalMedia image = getDatas().get(showCamera ? position - 1 : position);
            RequestOptions requestOptions = ImageUtil.getRequestOptions(R.drawable.image_placeholder);
            requestOptions = requestOptions.override(RuntimeManager.getInstance().getScreenWidth() / 4);
            ImageUtil.loadImage(image.getPath(), contentHolder.picture, requestOptions);
            if (selectMode == ImageSelectorActivity.MODE_SINGLE) {
                contentHolder.check.setVisibility(View.GONE);
            }
            selectImage(contentHolder, isSelected(image));
            if (enablePreview) {
                contentHolder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeCheckboxState(contentHolder, image);
                    }
                });
            }
            contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((selectMode == ImageSelectorActivity.MODE_SINGLE || enablePreview) && imageSelectChangedListener != null) {
                        imageSelectChangedListener.onPictureClick(image, showCamera ? holder.getAdapterPosition() - 1 : holder.getAdapterPosition());
                    } else {
                        changeCheckboxState(contentHolder, image);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showCamera ? getDatas().size() + 1 : super.getItemCount();
    }

    private void changeCheckboxState(ViewHolder contentHolder, LocalMedia image) {
        boolean isChecked = contentHolder.check.isSelected();
        if (selectImages.size() >= maxSelectNum && !isChecked) {
            CustomToast.getInstance().showToast(context.getString(R.string.message_max_num, maxSelectNum));
            return;
        }
        if (isChecked) {
            setCheckBoxUnCheckedIcon(contentHolder.check);
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    break;
                }
            }
        } else {
            setCheckBoxCheckedIcon(contentHolder.check);
            selectImages.add(image);
        }
        selectImage(contentHolder, !isChecked);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    public void setCheckBoxCheckedIcon(View checkBox){
        Drawable selectDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_checked));
        DrawableCompat.setTint(selectDrawable, GetAppColor.getInstance().getAppColor());
        checkBox.setBackground(selectDrawable);
    }

    public void setCheckBoxUnCheckedIcon(View checkBox){
        checkBox.setBackgroundResource(R.drawable.ic_check);
    }

    public List<LocalMedia> getSelectedImages() {
        return selectImages;
    }

    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    public void selectImage(ViewHolder holder, boolean isChecked) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            setCheckBoxCheckedIcon(holder.check);
            holder.picture.setColorFilter(context.getResources().getColor(R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            setCheckBoxUnCheckedIcon(holder.check);
            holder.picture.setColorFilter(context.getResources().getColor(R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setOnImageSelectChangedListener(OnImageSelectChangedListener imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }

    public interface OnImageSelectChangedListener {
        void onChange(List<LocalMedia> selectImages);

        void onTakePhoto();

        void onPictureClick(LocalMedia media, int position);
    }

    public static class HeaderViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
        View headerView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
        }
    }

    public static class ViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
        ImageView picture;
        ImageView check;

        View contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = itemView.findViewById(R.id.picture);
            check = itemView.findViewById(R.id.check);
        }
    }
}
