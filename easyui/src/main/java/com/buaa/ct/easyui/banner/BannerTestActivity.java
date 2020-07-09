package com.buaa.ct.easyui.banner;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;

import java.util.ArrayList;
import java.util.List;

public class BannerTestActivity extends CoreBaseActivity {
    BannerView bannerView;

    @Override
    public int getLayoutId() {
        return R.layout.banner_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        bannerView = findViewById(R.id.banner);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.banner_a);
        list.add(R.drawable.banner_b);
        list.add(R.drawable.banner_c);
        list.add(R.drawable.banner_d);
        list.add(R.drawable.banner_e);
        list.add(R.drawable.banner_f);
        list.add(R.drawable.banner_g);
        list.add(R.drawable.banner_h);
        list.add(R.drawable.banner_i);
        bannerView.initData(list);

        title.setText(R.string.banner_test);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bannerView.stopAd();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bannerView.startAd();
    }
}
