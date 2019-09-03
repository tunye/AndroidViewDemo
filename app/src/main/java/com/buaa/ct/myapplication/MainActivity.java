package com.buaa.ct.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.myapplication.adapter.EnterAdapter;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

public class MainActivity extends BaseActivity {
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
    protected void onActivityCreated() {
        super.onActivityCreated();
        recyclerView.setAdapter(new EnterAdapter(this));
        title.setText(R.string.app_name);
        back.setVisibility(View.INVISIBLE);
    }
}
