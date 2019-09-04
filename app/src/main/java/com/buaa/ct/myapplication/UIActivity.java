package com.buaa.ct.myapplication;

import android.support.v7.widget.RecyclerView;

import com.buaa.ct.myapplication.adapter.UIEnterAdapter;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

public class UIActivity extends BaseActivity {
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.enter);
        setRecyclerViewProperty(recyclerView);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.ui);
        recyclerView.setAdapter(new UIEnterAdapter(this));
    }
}
