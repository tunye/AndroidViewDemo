package com.buaa.ct.easyui.Banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.buaa.ct.easyui.R;

import java.util.ArrayList;
import java.util.List;

public class BannerTestActivity extends AppCompatActivity {
    BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_test);
        bannerView = findViewById(R.id.banner);
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
