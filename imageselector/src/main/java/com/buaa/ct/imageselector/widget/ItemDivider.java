package com.buaa.ct.imageselector.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.imageselector.R;

public class ItemDivider extends RecyclerView.ItemDecoration {
    private Drawable mDrawable;

    public ItemDivider(Context context) {
        mDrawable = context.getResources().getDrawable(R.drawable.item_divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent) {
        final int left = RuntimeManager.getInstance().dip2px(16);
        final int right = parent.getWidth() - left;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, int position, @NonNull RecyclerView parent) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicWidth());
    }
}