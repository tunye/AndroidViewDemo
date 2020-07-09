package com.buaa.ct.easyui.recycler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.SkidRightLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SkidTestActivity extends CoreBaseActivity {
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.recycler_skid_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.skid_recycler_view);
        recyclerView.setLayoutManager(new SkidRightLayoutManager(1.5f, 0.85f));
        recyclerView.setAdapter(new MyAdapter());
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_skid);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] imgs = {
                R.drawable.skid_right_1,
                R.drawable.skid_right_2,
                R.drawable.skid_right_3,
                R.drawable.skid_right_4,
                R.drawable.skid_right_5,
                R.drawable.skid_right_6,
                R.drawable.skid_right_1

        };
        String[] titles = {"Acknowledge", "Belief", "Confidence", "Dreaming", "Happiness", "Confidence"};

        @Override
        public @NonNull
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skid, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).asGif().load(imgs[position % getRealCount()]).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.imgBg);
            holder.tvTitle.setText(titles[position % getRealCount()]);
            holder.imgBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), SkidDetailActivity.class);
                    intent.putExtra("img", imgs[holder.getAdapterPosition() % getRealCount()]);
                    intent.putExtra("title", titles[holder.getAdapterPosition() % getRealCount()]);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SkidTestActivity.this, Pair.create((View) holder.imgBg, "img_view_1"), Pair.create((View) holder.tvTitle, "title_1"), Pair.create((View) holder.tvBottom, "tv_bottom"));

                    holder.itemView.getContext().startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return getRealCount() * 4;
        }

        public int getRealCount() {
            return imgs.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBg;
            TextView tvTitle;
            TextView tvBottom;

            public ViewHolder(View itemView) {
                super(itemView);
                imgBg = itemView.findViewById(R.id.img_bg);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvBottom = itemView.findViewById(R.id.tv_bottom);
            }
        }
    }
}
