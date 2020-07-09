package com.buaa.ct.easyui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.ViewPagerLayoutManager;

public class ViewPagerTestActivity extends CoreBaseActivity {
    RecyclerView recyclerView;
    ViewPagerLayoutManager layoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.recycler_echelon_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.echelon_recycler_view);
        layoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());
    }

    @Override
    public void setListener() {
        super.setListener();
        layoutManager.setOnViewPagerListener(new ViewPagerLayoutManager.OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {

            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                CustomToast.getInstance().showToast("展示" + position + "页");
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_view_pager);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] bgs = {R.drawable.img_slide_1, R.drawable.img_slide_2, R.drawable.img_slide_3, R.drawable.img_slide_4, R.drawable.img_slide_5, R.drawable.img_slide_6,};
        private String[] descs = {
                "人生就像迷宫，我们用上半生找寻入口，用下半生找寻出口",
                "原来地久天长，只是误会一场",
                "不是故事的结局不够好，而是我们对故事的要求过多",
                "只想优雅转身，不料华丽撞墙",
                "忘了这一年，我最开心的一年",
                "回去安安分分的当个皇后"
        };

        @Override
        public @NonNull
        MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.desc.setText(descs[position % getRealCount()]);
            holder.bg.setImageResource(bgs[position % getRealCount()]);
        }

        @Override
        public int getItemCount() {
            return 60;
        }

        public int getRealCount() {
            return bgs.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView bg;
            TextView desc;

            public ViewHolder(View itemView) {
                super(itemView);
                bg = itemView.findViewById(R.id.view_pager_bg);
                desc = itemView.findViewById(R.id.view_pager_title);
            }
        }
    }
}
