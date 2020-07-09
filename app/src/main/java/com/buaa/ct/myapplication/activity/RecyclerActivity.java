package com.buaa.ct.myapplication.activity;

import android.support.v7.widget.RecyclerView;

import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.adapter.RecyclerEnterAdapter;
import com.buaa.ct.myapplication.adapter.UIEnterAdapter;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.pudding.util.PuddingBuilder;

public class RecyclerActivity extends BaseActivity {
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.enter);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_recycler);
        recyclerView.setAdapter(new RecyclerEnterAdapter(this));
        setRecyclerViewProperty(recyclerView);
    }

    @Override
    public void onShot(String imgPath) {
        super.onShot(imgPath);
        new PuddingBuilder().setTitleText("有一张新的截图，路径是")
                .setSubTitleText(imgPath)
                .setEnableInfiniteDuration()
                .setEnableIconAnimation().create(this).show();
    }
}
