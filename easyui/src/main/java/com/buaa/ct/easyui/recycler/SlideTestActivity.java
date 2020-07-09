package com.buaa.ct.easyui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.SlideLayoutManager;
import com.buaa.ct.easyui.recycler.layoutmanager.entity.ItemConfig;
import com.buaa.ct.easyui.recycler.layoutmanager.entity.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class SlideTestActivity extends CoreBaseActivity {
    RecyclerView recyclerView;
    ItemTouchHelper mItemTouchHelper;
    ItemTouchHelperCallback mItemTouchHelperCallback;
    List<SlideBean> mList = new ArrayList<>();
    MyAdapter myAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.recycler_echelon_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.echelon_recycler_view);
        myAdapter = new MyAdapter();
        mItemTouchHelperCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        recyclerView.setLayoutManager(new SlideLayoutManager(recyclerView, mItemTouchHelper));

        recyclerView.setAdapter(myAdapter);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        addData();
    }

    @Override
    public void setListener() {
        super.setListener();
        mItemTouchHelperCallback.setOnSlideListener(new ItemTouchHelperCallback.OnSlideListener() {
            @Override
            public void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                String direct = "";
                if (direction == ItemConfig.SLIDING_LEFT) {
                    direct = "LEFT";
                } else if (direction == ItemConfig.SLIDING_RIGHT) {
                    direct = "RIGHT";
                }
            }

            @Override
            public void onSlided(RecyclerView.ViewHolder viewHolder, int layoutPos, int direction) {
                String direct = "";
                if (direction == ItemConfig.SLIDED_LEFT) {
//                    mDislikeCount--;
//                    mSmileView.setDisLike(mDislikeCount);
//                    mSmileView.disLikeAnimation();
                    direct = "LEFT";
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
//                    mLikeCount++;
//                    mSmileView.setLike(mLikeCount);
//                    mSmileView.likeAnimation();
                    direct = "RIGHT";
                }
                mList.remove(layoutPos);
                myAdapter.setmList(mList);
            }
        });
        toolbarOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_slide);
        enableToolbarOper("恢复数据");
    }

    private void addData() {
        int[] icons = {R.drawable.header_icon_1, R.drawable.header_icon_2, R.drawable.header_icon_3,
                R.drawable.header_icon_4, R.drawable.header_icon_1, R.drawable.header_icon_2};
        String[] titles = {"Acknowledging", "Belief", "Confidence", "Dreaming", "Happiness", "Confidence"};
        String[] says = {
                "Do one thing at a time, and do well.",
                "Keep on going never give up.",
                "Whatever is worth doing is worth doing well.",
                "I can because i think i can.",
                "Jack of all trades and master of none.",
                "Keep on going never give up.",
                "Whatever is worth doing is worth doing well.",
        };
        int[] bgs = {
                R.drawable.img_slide_1,
                R.drawable.img_slide_2,
                R.drawable.img_slide_3,
                R.drawable.img_slide_4,
                R.drawable.img_slide_5,
                R.drawable.img_slide_6
        };

        for (int i = 0; i < 6; i++) {
            mList.add(new SlideBean(bgs[i], titles[i], icons[i], says[i]));
        }
        myAdapter.setmList(mList);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        List<SlideBean> mList = new ArrayList<>();

        public void setmList(List<SlideBean> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public @NonNull
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SlideBean bean = mList.get(position);
            holder.imgBg.setImageResource(bean.getItemBg());
            holder.tvTitle.setText(bean.getTitle());
            holder.userIcon.setImageResource(bean.getUserIcon());
            holder.userSay.setText(bean.getUserSay());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBg;
            ImageView userIcon;
            TextView tvTitle;
            TextView userSay;

            public ViewHolder(View itemView) {
                super(itemView);
                imgBg = itemView.findViewById(R.id.img_bg);
                userIcon = itemView.findViewById(R.id.img_user);
                tvTitle = itemView.findViewById(R.id.tv_title);
                userSay = itemView.findViewById(R.id.tv_user_say);
            }
        }
    }

    public static class SlideBean {
        private int mItemBg;
        private String mTitle;
        private int mUserIcon;
        private String mUserSay;

        public SlideBean(int mItemBg, String mTitle, int mUserIcon, String mUserSay) {
            this.mItemBg = mItemBg;
            this.mTitle = mTitle;
            this.mUserIcon = mUserIcon;
            this.mUserSay = mUserSay;
        }

        public int getItemBg() {
            return mItemBg;
        }

        public void setItemBg(int mItemBg) {
            this.mItemBg = mItemBg;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public int getUserIcon() {
            return mUserIcon;
        }

        public void setUserIcon(int mUserIcon) {
            this.mUserIcon = mUserIcon;
        }

        public String getUserSay() {
            return mUserSay;
        }

        public void setUserSay(String mUserSay) {
            this.mUserSay = mUserSay;
        }
    }

}
