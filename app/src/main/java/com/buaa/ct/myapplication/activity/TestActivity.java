package com.buaa.ct.myapplication.activity;

import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

public class TestActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText("测试页面");
    }
}
