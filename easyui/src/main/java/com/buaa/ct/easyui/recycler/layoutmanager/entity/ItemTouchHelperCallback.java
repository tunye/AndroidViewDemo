package com.buaa.ct.easyui.recycler.layoutmanager.entity;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.easyui.recycler.layoutmanager.SlideLayoutManager;

import java.util.List;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private OnSlideListener mListener;

    public void setOnSlideListener(OnSlideListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int slideFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof SlideLayoutManager) {
            slideFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }
        return makeMovementFlags(dragFlags, slideFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        viewHolder.itemView.setOnTouchListener(null);
        int layoutPosition = viewHolder.getLayoutPosition();
        if (mListener != null) {
            mListener.onSlided(viewHolder, layoutPosition, direction == ItemTouchHelper.LEFT ? ItemConfig.SLIDED_LEFT : ItemConfig.SLIDED_RIGHT);
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float ratio = dX / getThreshold(recyclerView, viewHolder);
            if (ratio > 1) {
                ratio = 1;
            } else if (ratio < -1) {
                ratio = -1;
            }
            itemView.setRotation(ratio * ItemConfig.DEFAULT_ROTATE_DEGREE);
            int childCount = recyclerView.getChildCount();
            if (childCount > ItemConfig.DEFAULT_SHOW_ITEM) {
                for (int position = 1; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    float scale = 1f - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE;
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                    view.setTranslationY((index - Math.abs(ratio)) * itemView.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y);
                }
            } else {
                for (int position = 0; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    float scale = 1f - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE;
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                    view.setTranslationY((index - Math.abs(ratio)) * itemView.getMeasuredHeight() / ItemConfig.DEFAULT_TRANSLATE_Y);
                }
            }
            if (mListener != null) {
                if (ratio != 0) {
                    mListener.onSliding(viewHolder, ratio, ratio < 0 ? ItemConfig.SLIDING_LEFT : ItemConfig.SLIDING_RIGHT);
                } else {
                    mListener.onSliding(viewHolder, ratio, ItemConfig.SLIDING_NONE);
                }
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0f);
    }

    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

    public interface OnSlideListener {

        void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction);

        void onSlided(RecyclerView.ViewHolder viewHolder, int layoutPos, int direction);
    }
}
