package com.buaa.ct.easyui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.EchelonLayoutManager;

public class EchelonTestActivity extends CoreBaseActivity {
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.recycler_echelon_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.echelon_recycler_view);
        recyclerView.setLayoutManager(new EchelonLayoutManager());
        recyclerView.setAdapter(new MyAdapter());
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_echelon);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] icons = {R.drawable.header_icon_1, R.drawable.header_icon_2, R.drawable.header_icon_3, R.drawable.header_icon_4};
        private int[] bgs = {R.drawable.img_slide_1, R.drawable.img_slide_2, R.drawable.img_slide_3, R.drawable.img_slide_4};
        private String[] nickNames = {"左耳近心", "凉雨初夏", "稚久九栀", "半窗疏影"};
        private String[] descs = {
                "人生就像迷宫，我们用上半生找寻入口，用下半生找寻出口",
                "原来地久天长，只是误会一场",
                "不是故事的结局不够好，而是我们对故事的要求过多",
                "只想优雅转身，不料华丽撞墙"
        };

        @Override
        public @NonNull
        MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_echelon, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.icon.setImageResource(icons[position % getRealCount()]);
            holder.nickName.setText(nickNames[position % getRealCount()]);
            holder.desc.setText(descs[position % getRealCount()]);
            holder.bg.setImageResource(bgs[position % getRealCount()]);
        }

        @Override
        public int getItemCount() {
            return 60;
        }

        public int getRealCount() {
            return icons.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            ImageView bg;
            TextView nickName;
            TextView desc;

            public ViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.img_icon);
                bg = itemView.findViewById(R.id.img_bg);
                nickName = itemView.findViewById(R.id.tv_nickname);
                desc = itemView.findViewById(R.id.tv_desc);
            }
        }
    }
}
