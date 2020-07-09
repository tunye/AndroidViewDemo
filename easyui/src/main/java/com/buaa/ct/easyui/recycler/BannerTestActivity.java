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
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.BannerLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class BannerTestActivity extends CoreBaseActivity {
    RecyclerView recyclerViewForBanner, recyclerViewForHotMsg;
    ImageView mImg1, mImg2, mImg3, mImg4;
    List<ImageView> mImgList = new ArrayList<>();

    private int mLastSelectPosition = 0;

    @Override
    public int getLayoutId() {
        return R.layout.recycler_banner_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerViewForBanner = findViewById(R.id.banner_recycler_view);
        recyclerViewForHotMsg = findViewById(R.id.ad_recycler_view);
        ((InterceptFrameLayout) findViewById(R.id.intercept_for_banner)).setIntercept(false);

        initIndictor();
        MyAdapter myAdapter = new MyAdapter();
        BannerLayoutManager bannerLayoutManager = new BannerLayoutManager(recyclerViewForBanner, myAdapter.getRealCount(), OrientationHelper.HORIZONTAL);
        recyclerViewForBanner.setLayoutManager(bannerLayoutManager);
        recyclerViewForBanner.setAdapter(myAdapter);
        bannerLayoutManager.setOnSelectedViewListener(new BannerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                changeUI(position);
            }
        });
        bannerLayoutManager.setTimeDelayed(3000);
        bannerLayoutManager.setTimeSmooth(250f);
        changeUI(0);

        MyNewsAdapter myNewsAdapter = new MyNewsAdapter();
        BannerLayoutManager bannerNewsLayoutManager = new BannerLayoutManager(recyclerViewForHotMsg, myNewsAdapter.getRealCount(), OrientationHelper.VERTICAL);
        bannerNewsLayoutManager.setTimeSmooth(400f);
        bannerNewsLayoutManager.setTimeDelayed(2000);
        recyclerViewForHotMsg.setLayoutManager(bannerNewsLayoutManager);
        recyclerViewForHotMsg.setAdapter(myNewsAdapter);
    }

    private void initIndictor() {
        mImg1 = findViewById(R.id.img_1);
        mImg2 = findViewById(R.id.img_2);
        mImg3 = findViewById(R.id.img_3);
        mImg4 = findViewById(R.id.img_4);
        mImgList.add(mImg1);
        mImgList.add(mImg2);
        mImgList.add(mImg3);
        mImgList.add(mImg4);
    }

    private void changeUI(int position) {
        if (position != mLastSelectPosition) {
            mImgList.get(position).setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
            mImgList.get(mLastSelectPosition).setImageDrawable(getResources().getDrawable(R.drawable.circle_gray));
            mLastSelectPosition = position;
        }
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_banner);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] imgs = {
                R.drawable.banner_a,
                R.drawable.banner_b,
                R.drawable.banner_c,
                R.drawable.banner_d,
        };

        @Override
        public @NonNull
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.img.setImageResource(imgs[position % getRealCount()]);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        public int getRealCount() {
            return imgs.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img;

            public ViewHolder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
            }
        }
    }

    private static class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.ViewHolder> {
        private String[] mTitles = {
                "小米8官方宣布有双路GPS,小米8、小米8SE发售时间曝光",
                "这样的锤子你玩懂了吗?坚果R1带来不一样的体验",
                "三星真的很爱酸苹果!新广告讽刺苹果手机电池降速事件",
                "双摄全面屏 游戏长续航 魅族科技发布魅蓝6T售799元起",
        };

        @Override
        public @NonNull
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_news, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.tv_news.setText(mTitles[position % getRealCount()]);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        public int getRealCount() {
            return mTitles.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_news;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_news = itemView.findViewById(R.id.tv_news);
            }
        }
    }
}
