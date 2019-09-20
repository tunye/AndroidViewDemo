package com.buaa.ct.myapplication;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.view.image.DividerItemDecoration;
import com.buaa.ct.myapplication.activity.SettingActivity;
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
    }

    @Override
    public void setListener() {
        super.setListener();
        toolbarOper.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SettingActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        recyclerView.setAdapter(new EnterAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        recyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.TYPE_WITH_BORDER));
        title.setText(R.string.app_name);
        back.setVisibility(View.INVISIBLE);
        enableToolbarOper(R.string.setting);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((MyApplication) getApplication()).clearActivityList();
    }
}
