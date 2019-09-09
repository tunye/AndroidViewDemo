package com.buaa.ct.imageselector.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.util.ImageUtil;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.model.LocalMediaFolder;

import java.util.List;

/**
 * Created by dee on 15/11/20.
 */
public class ImageFolderAdapter extends CoreRecyclerViewAdapter<LocalMediaFolder, ImageFolderAdapter.ViewHolder> {
    private int checkedIndex = 0;

    public ImageFolderAdapter(Context context) {
        super(context);
    }

    public void setFolders(List<LocalMediaFolder> folders) {
        setDataSet(folders);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final LocalMediaFolder folder = getDatas().get(position);
        ImageUtil.loadImage(folder.getFirstImagePath(), holder.firstImage, ImageUtil.getRequestOptions(R.drawable.ic_placeholder));
        holder.folderName.setText(folder.getName());
        holder.imageNum.setText(context.getString(R.string.num_postfix, folder.getImageNum()));
        holder.isSelected.setVisibility(checkedIndex == position ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(int pos) {
        int lastPos = checkedIndex;
        checkedIndex = pos;
        notifyItemChanged(pos);
        notifyItemChanged(lastPos);
    }

    public static class ViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
        ImageView firstImage;
        TextView folderName;
        TextView imageNum;
        ImageView isSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            firstImage = itemView.findViewById(R.id.first_image);
            folderName = itemView.findViewById(R.id.folder_name);
            imageNum = itemView.findViewById(R.id.image_num);
            isSelected = itemView.findViewById(R.id.is_selected);
            Drawable selectDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_radio_button));
            DrawableCompat.setTint(selectDrawable, GetAppColor.getInstance().getAppColor());
            isSelected.setImageDrawable(selectDrawable);
        }
    }
}
