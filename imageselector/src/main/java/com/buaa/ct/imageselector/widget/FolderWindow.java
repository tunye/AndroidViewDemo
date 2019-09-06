package com.buaa.ct.imageselector.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.buaa.ct.core.listener.OnRecycleViewItemClickListener;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.adapter.ImageFolderAdapter;
import com.buaa.ct.imageselector.model.LocalMediaFolder;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dee on 15/11/20.
 */
public class FolderWindow extends PopupWindow {
    private Context context;
    private View window;
    private RecyclerView recyclerView;
    private ImageFolderAdapter adapter;

    private boolean isDismiss = false;

    public FolderWindow(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.window_folder, null);
        this.setContentView(window);
        this.setWidth(RuntimeManager.getInstance().getScreenWidth());
        this.setHeight(RuntimeManager.getInstance().getScreenHeight() - RuntimeManager.getInstance().dip2px(96));
        setPopupWindowTouchModal(this, false);
        this.setAnimationStyle(R.style.WindowStyle);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(153, 0, 0, 0)));

        initView();
        registerListener();
    }

    public static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        adapter = new ImageFolderAdapter(context);

        recyclerView = window.findViewById(R.id.folder_list);
        recyclerView.addItemDecoration(new ItemDivider(context));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void registerListener() {

    }

    public void bindFolder(List<LocalMediaFolder> folders) {
        adapter.setFolders(folders);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_in);
        recyclerView.startAnimation(animation);
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    public LocalMediaFolder getCurFolderInfo(int pos) {
        return adapter.getDatas().get(pos);
    }

    @Override
    public void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_out);
        recyclerView.startAnimation(animation);
        dismiss();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDismiss = false;
                FolderWindow.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static class ItemDivider extends RecyclerView.ItemDecoration {
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
}
