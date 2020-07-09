package com.buaa.ct.easyui.recycler;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.GallerySnapHelper;

import java.util.ArrayList;
import java.util.List;

public class MultiUserTestActivity extends CoreBaseActivity {
    public static final int PADDING = RuntimeManager.getInstance().dip2px(12);
    public static final int ROW = 3;
    RecyclerView recyclerView;
    RecyclerView backUp;
    GridLayoutManager layoutManager;

    GallerySnapHelper gallerySnapHelper;
    LinearSnapHelper linearSnapHelper;
    boolean galleryMode;

    @Override
    public int getLayoutId() {
        return R.layout.recycler_multi_user;
    }

    @Override
    public void beforeSetLayout(Bundle savedInstanceState) {
        super.beforeSetLayout(savedInstanceState);
        gallerySnapHelper = new GallerySnapHelper();
        linearSnapHelper = new LinearSnapHelper();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        backUp = new RecyclerView(context);
        recyclerView = findViewById(R.id.enter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(PADDING, PADDING / 2));
        layoutManager = new GridLayoutManager(context, ROW, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());

        gallerySnapHelper.attachToRecyclerView(recyclerView);
        gallerySnapHelper.setGalleryMode(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        toolbarOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryMode = !galleryMode;
                gallerySnapHelper.setGalleryMode(galleryMode);
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_multi_user);
        enableToolbarOper("更换模式");
    }

    private static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int vSpacing;
        private final int hSpacing;

        private GridSpacingItemDecoration(int hSpacing, int vSpacing) {
            this.hSpacing = hSpacing;
            this.vSpacing = vSpacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = hSpacing;
            outRect.top = vSpacing; // item top
            outRect.bottom = vSpacing;
            outRect.right = hSpacing;
        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<User> users = new ArrayList<>();

        public MyAdapter() {
            users.clear();
            users.add(new User("愤怒的企鹅", "我其实只是一只憨憨的企鹅", R.drawable.header_icon_1));
            users.add(new User("进击的阿里", "工作要996，生活要669", R.drawable.header_icon_2));
            users.add(new User("衰退的度娘", "各个公司都用多少度衡量自己，实在是太惨了", R.drawable.header_icon_3));
            users.add(new User("字节的跳动", "偷偷摸摸发财", R.drawable.header_icon_4));
        }

        @Override
        public @NonNull
        MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_user, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            final User user = users.get(position % getRealCount());
            holder.icon.setImageResource(user.avatar);
            holder.nickName.setText(user.name + " " + position);
            holder.desc.setText(user.desc);
            holder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomToast.getInstance().showToast(user.name + "已关注");
                    holder.follow.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 60;
        }

        public int getRealCount() {
            return users.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView nickName;
            TextView desc;
            TextView follow;

            public ViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.user_avatar);
                nickName = itemView.findViewById(R.id.user_name);
                desc = itemView.findViewById(R.id.user_desc);
                follow = itemView.findViewById(R.id.user_follow);
            }
        }
    }

    public static class User {
        public String name;
        public String desc;
        public @DrawableRes
        int avatar;

        public User(String name, String desc, int avatar) {
            this.name = name;
            this.desc = desc;
            this.avatar = avatar;
        }
    }
}
