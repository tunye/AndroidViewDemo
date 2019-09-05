package com.buaa.ct.myapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
        toolbarOper.setText(R.string.setting);
        setRecyclerViewProperty(recyclerView);
    }

    @Override
    public void setListener() {
        super.setListener();
        toolbarOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SettingActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        recyclerView.setAdapter(new EnterAdapter(this));
        title.setText(R.string.app_name);
        back.setVisibility(View.INVISIBLE);
    }
}
