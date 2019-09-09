package com.buaa.ct.myapplication.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.buaa.ct.core.view.image.DividerItemDecoration;
import com.buaa.ct.myapplication.R;
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
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.ui);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
        recyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.TYPE_WITH_BORDER));
        recyclerView.setAdapter(new UIEnterAdapter(this));
    }
}
